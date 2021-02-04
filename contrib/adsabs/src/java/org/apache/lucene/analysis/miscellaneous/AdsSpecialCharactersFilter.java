package org.apache.lucene.analysis.miscellaneous;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.RamUsageEstimator;

/**
 * 
 * This is a modified ASCIIFoldingFilter
 * 
 * It translates greek math symbols
 * 
 * For example, 'γ' will be replaced by 'gamma'.
 */
public final class AdsSpecialCharactersFilter extends TokenFilter {
  public AdsSpecialCharactersFilter(TokenStream input)
  {
    super(input);
  }

  private char[] output = new char[512];
  private int outputPos;
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

  @Override
  public boolean incrementToken() throws IOException {
    if (input.incrementToken()) {
      final char[] buffer = termAtt.buffer();
      final int length = termAtt.length();

      // If no characters actually require rewriting then we
      // just return token as-is:
      for(int i = 0 ; i < length ; ++i) {
        final char c = buffer[i];
        if (c >= '\u0080')
        {
          foldToASCII(buffer, length);
          termAtt.copyBuffer(output, 0, outputPos);
          break;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Converts characters above ASCII to their ASCII equivalents.  For example,
   * accents are removed from accented characters.
   * @param input The string to fold
   * @param length The number of characters in the input string
   */
  public void foldToASCII(char[] input, int length)
  {
    // Worst-case length required:
    final int maxSizeNeeded = 4 * length;
    if (output.length < maxSizeNeeded) {
      output = new char[ArrayUtil.oversize(maxSizeNeeded, Character.BYTES)];
    }

    outputPos = foldToASCII(input, 0, output, 0, length);
  }

  /**
   * Converts characters above ASCII to their ASCII equivalents.  For example,
   * accents are removed from accented characters.
   * @param input     The characters to fold
   * @param inputPos  Index of the first character to fold
   * @param output    The result of the folding. Should be of size &gt;= {@code length * 4}.
   * @param outputPos Index of output where to put the result of the folding
   * @param length    The number of characters to fold
   * @return length of output
   * @lucene.internal
   */
  public static final int foldToASCII(char input[], int inputPos, char output[], int outputPos, int length)
  {
    final int end = inputPos + length;
    for (int pos = inputPos; pos < end ; ++pos) {
      final char c = input[pos];

      // Quick test: if it's not in range then just keep current character
      if (c < '\u0080') {
        output[outputPos++] = c;
      } else {
        switch (c) {
          case '\u0391':
          case '\u03B1': 
            output[outputPos++] = 'a';
            output[outputPos++] = 'l';
            output[outputPos++] = 'p';
            output[outputPos++] = 'h';
            output[outputPos++] = 'a';
            break;
          case '\u0392':
          case '\u03B2': 
            output[outputPos++] = 'b';
            output[outputPos++] = 'e';
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            break;
          case '\u0393':
          case '\u03B3': 
            output[outputPos++] = 'g';
            output[outputPos++] = 'a';
            output[outputPos++] = 'm';
            output[outputPos++] = 'm';
            output[outputPos++] = 'a';
            break;
          case '\u0394':
          case '\u03B4': 
            output[outputPos++] = 'd';
            output[outputPos++] = 'e';
            output[outputPos++] = 'l';
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            break;
          case '\u0395':
          case '\u03B5': 
            output[outputPos++] = 'e';
            output[outputPos++] = 'p';
            output[outputPos++] = 's';
            output[outputPos++] = 'i';
            output[outputPos++] = 'l';
            output[outputPos++] = 'o';
            output[outputPos++] = 'n';
            break;
          case '\u0396':
          case '\u03B6': 
            output[outputPos++] = 'z';
            output[outputPos++] = 'e';
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            break;
          case '\u0397':
          case '\u03B7': 
            output[outputPos++] = 'e';
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            break;
          case '\u0398':
          case '\u03B8': 
            output[outputPos++] = 't';
            output[outputPos++] = 'h';
            output[outputPos++] = 'e';
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            break;
          case '\u0399':
          case '\u03B9': 
            output[outputPos++] = 'i';
            output[outputPos++] = 'o';
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            break;
          case '\u039A':
          case '\u03BA': 
            output[outputPos++] = 'k';
            output[outputPos++] = 'a';
            output[outputPos++] = 'p';
            output[outputPos++] = 'p';
            output[outputPos++] = 'a';
            break;
          case '\u039B':
          case '\u03BB': 
            output[outputPos++] = 'l';
            output[outputPos++] = 'a';
            output[outputPos++] = 'm';
            output[outputPos++] = 'b';
            output[outputPos++] = 'd';
            output[outputPos++] = 'a';
            break;
          case '\u039C':
          case '\u03BC': 
            output[outputPos++] = 'm';
            output[outputPos++] = 'u';
            break;
          case '\u039D':
          case '\u03BD': 
            output[outputPos++] = 'n';
            output[outputPos++] = 'u';
            break;
          case '\u039E':
          case '\u03BE': 
            output[outputPos++] = 'x';
            output[outputPos++] = 'i';
            break;
          case '\u039F':
          case '\u03BF': 
            output[outputPos++] = 'o';
            output[outputPos++] = 'm';
            output[outputPos++] = 'i';
            output[outputPos++] = 'c';
            output[outputPos++] = 'r';
            output[outputPos++] = 'o';
            output[outputPos++] = 'n';
            break;
          case '\u03A0':
          case '\u03C0': 
            output[outputPos++] = 'p';
            output[outputPos++] = 'i';
            break;
          case '\u03A1':
          case '\u03C1':
            output[outputPos++] = 'r';
            output[outputPos++] = 'h';
            output[outputPos++] = 'o';
            break;
          case '\u03A3':
          case '\u03C3':
            output[outputPos++] = 's';
            output[outputPos++] = 'i';
            output[outputPos++] = 'g';
            output[outputPos++] = 'm';
            output[outputPos++] = 'a';
            break;
          case '\u03A4':
          case '\u03C4': 
            output[outputPos++] = 't';
            output[outputPos++] = 'a';
            output[outputPos++] = 'u';
            break;
          case '\u03A5':
          case '\u03C5': 
            output[outputPos++] = 'u';
            output[outputPos++] = 'p';
            output[outputPos++] = 's';
            output[outputPos++] = 'i';
            output[outputPos++] = 'l';
            output[outputPos++] = 'o';
            output[outputPos++] = 'n';
            break;
          case '\u03A6':
          case '\u03C6': 
            output[outputPos++] = 'p';
            output[outputPos++] = 'h';
            output[outputPos++] = 'i';
            break;
          case '\u03A7':
          case '\u03C7': 
            output[outputPos++] = 'c';
            output[outputPos++] = 'h';
            output[outputPos++] = 'i';
            break;
          case '\u03A8':
          case '\u03C8': 
            output[outputPos++] = 'p';
            output[outputPos++] = 's';
            output[outputPos++] = 'i';
            break;
          case '\u03A9':
          case '\u03C9': 
            output[outputPos++] = 'o';
            output[outputPos++] = 'm';
            output[outputPos++] = 'e';
            output[outputPos++] = 'g';
            output[outputPos++] = 'a';
            break;
          default:
            output[outputPos++] = c;
            break;
        }
      }
    }
    return outputPos;
  }
}
