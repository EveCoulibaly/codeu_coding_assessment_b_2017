// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.*;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {

  private final String source;
  private int position;
  private StringBuilder token;

  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source;
    token = new StringBuilder();
    position = 0;
  }

  // NEXT
  //
  // Get the next token in the stream. When the end of stream has been reached
  // |next| should return |null|. The only valid tokens that can be returned are:
  //  - com.google.codeu.mathlang.core.tokens.StringToken
  //  - com.google.codeu.mathlang.core.tokens.NameToken
  //  - com.google.codeu.mathlang.core.tokens.SymbolToken
  //  - com.google.codeu.mathlang.core.tokens.NumberToken
  // If there is ever a problem with the source data, |next| should throw an
  // IOException.

  // Most of your work will take place here. For every call to |next| you should
  // return a token until you reach the end. When there are no more tokens, you
  // should return |null| to signal the end of input.

  // If for any reason you detect an error in the input, you may throw an IOException
  // which will stop all execution.

  @Override
  public Token next() throws IOException {

        String current;
        // Skip all leading whitespace
        while (remaining() > 0 && Character.isWhitespace(peek())) {
          read();  // ignore the result because we already know that it is a whitespace character
        }
        if (remaining() <= 0) {
          return null;
        } else if (peek() == '\"') {
          // read a token that is surrounded by quotes
          current = readWithQuotes();
          return new StringToken(current);
        } else if (Character.isDigit(peek())){
          current = readNumber();
          double value = Double.parseDouble(current);
          return new NumberToken(value);
        }
        else {
          // read a token that is not surrounded by quotes
          current = readWithNoQuotes();

          if (isName(current)) {
            return new NameToken(current);
          } else if (isSymbol(current)) {
            return new SymbolToken(current.charAt(0));
          } else {
            throw new IOException("Invalid input");
          }
        }
  }


  private char peek() throws IOException {
    if (position < source.length()) {
      return source.charAt(position);
    } else {
      throw new IOException("No characters");
    }
  }

  private char read() throws IOException {
  final char c = peek();
    position += 1;
    return c;
  }

  private String readWithNoQuotes() throws IOException {
    token.setLength(0);  // clear the token
    while (remaining() > 0 && !Character.isWhitespace(peek())) {
      token.append(read());
    }
    return token.toString();
  }

  private String readNumber() throws IOException {
    token.setLength(0);  // clear the token

    while (remaining() > 0 && Character.isDigit(peek())) {
      token.append(read());
    }
    return token.toString();
  }

  private String readWithQuotes() throws IOException {
    token.setLength(0);  // clear the token
    if (read() != '\"') {
      throw new IOException("Strings must start with opening quote");
    }
    while (peek() != '\"') {
      token.append(read());
    }
    read(); // read the closing the quote that allowed us to exit the loop
    return token.toString();
  }

  private int remaining() {
    return source.length() - position;
  }

  private boolean isSymbol(String input){
    return (!Character.isAlphabetic(input.charAt(0)) &&
            !Character.isDigit(input.charAt(0)) &&
            input.charAt(0) != '.');
  }

  private boolean isName(String input) {
    return Character.isAlphabetic(input.charAt(0));
  }

}
