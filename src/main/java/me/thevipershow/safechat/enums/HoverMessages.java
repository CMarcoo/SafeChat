/*
 * The MIT License
 *
 * Copyright 2020 marco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.thevipershow.safechat.enums;

public enum HoverMessages {

    NO_ARGS("&eSafeChat &7syntax:",
            "&6Open this help page&7:",
            "- &7/safechat",
            "&6Get stored data of a certain player&7:",
            "- &7/safechat sql search &8<&eplayer&8>",
            "&6Get &o&nX&r &6players with the highest flags count&7:",
            "- &7/safechat sql top &8<&enumber&8>",
            "&6Reload all the values from the config.yml",
            "- &7/safechat reload");

    private final String[] messages;

    HoverMessages(String... messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
