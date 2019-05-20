/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.common.node.utils;

import me.lucko.luckperms.api.node.Node;
import me.lucko.luckperms.api.node.types.MetaNode;
import me.lucko.luckperms.api.node.types.PrefixNode;
import me.lucko.luckperms.api.node.types.SuffixNode;

/**
 * Represents a type of meta
 */
public enum MetaType {

    /**
     * Represents any meta type
     */
    ANY {
        @Override
        public boolean matches(Node node) {
            return META.matches(node) || PREFIX.matches(node) || SUFFIX.matches(node);
        }
    },

    /**
     * Represents any chat meta type
     */
    CHAT {
        @Override
        public boolean matches(Node node) {
            return PREFIX.matches(node) || SUFFIX.matches(node);
        }
    },

    /**
     * Represents a meta key-value pair
     */
    META {
        @Override
        public boolean matches(Node node) {
            return node instanceof MetaNode;
        }
    },

    /**
     * Represents a prefix
     */
    PREFIX {
        @Override
        public boolean matches(Node node) {
            return node instanceof PrefixNode;
        }
    },

    /**
     * Represents a suffix
     */
    SUFFIX {
        @Override
        public boolean matches(Node node) {
            return node instanceof SuffixNode;
        }
    };

    /**
     * Returns if the passed node matches the type
     *
     * @param node the node to test
     * @return true if the node has the same type
     */
    public abstract boolean matches(Node node);

}
