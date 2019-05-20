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

package me.lucko.luckperms.api.caching;

import me.lucko.luckperms.api.PermissionHolder;
import me.lucko.luckperms.api.query.QueryOptions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

/**
 * Holds cached permission and meta lookup data for a {@link PermissionHolder}.
 *
 * <p>All calls will account for inheritance, as well as any default data
 * provided by the platform. This calls are heavily cached and are therefore
 * fast.</p>
 *
 * @since 4.0
 */
public interface CachedData {

    /**
     * Gets the manager for {@link PermissionData}.
     *
     * @return the permission data manager
     */
    @NonNull Manager<PermissionData> permissionData();

    /**
     * Gets the manager for {@link MetaData}.
     *
     * @return the meta data manager
     */
    @NonNull Manager<MetaData> metaData();

    /**
     * Gets PermissionData from the cache, given a specified context.
     *
     * @param queryOptions the query options
     * @return a permission data instance
     * @throws NullPointerException if contexts is null
     */
    @NonNull PermissionData getPermissionData(@NonNull QueryOptions queryOptions);

    /**
     * Gets MetaData from the cache, given a specified context.
     *
     * @param queryOptions the query options
     * @return a meta data instance
     * @throws NullPointerException if contexts is null
     */
    @NonNull MetaData getMetaData(@NonNull QueryOptions queryOptions);

    /**
     * Invalidates all cached {@link PermissionData} and {@link MetaData}
     * instances.
     *
     * @since 5.0
     */
    void invalidate();

    /**
     * Invalidates all of the underlying Permission calculators.
     *
     * <p>Can be called to allow for an update in defaults.</p>
     */
    void invalidatePermissionCalculators();

    /**
     * Manages a specific type of {@link CachedDataContainer cached data} within
     * a {@link CachedData} instance.
     *
     * @param <T> the data type
     */
    interface Manager<T extends CachedDataContainer> {

        /**
         * Gets {@link T data} from the cache.
         *
         * @param queryOptions the query options
         * @return a data instance
         * @throws NullPointerException if contexts is null
         */
        @NonNull T get(@NonNull QueryOptions queryOptions);

        /**
         * Calculates {@link T data}, bypassing the cache.
         *
         * <p>The result of this operation is calculated each time the method is called.
         * The result is not added to the internal cache.</p>
         *
         * <p>It is therefore highly recommended to use {@link #get(QueryOptions)} instead.</p>
         *
         * <p>The use cases of this method are more around constructing one-time
         * instances of {@link T data}, without adding the result to the cache.</p>
         *
         * @param queryOptions the query options
         * @return a data instance
         * @throws NullPointerException if contexts is null
         */
        @NonNull T calculate(@NonNull QueryOptions queryOptions);

        /**
         * (Re)calculates data for a given context.
         *
         * <p>This method returns immediately in all cases. The (re)calculation is
         * performed asynchronously and applied to the cache in the background.</p>
         *
         * <p>If there was a previous data instance associated with
         * the given {@link QueryOptions}, then that instance will continue to be returned by
         * {@link #get(QueryOptions)} until the recalculation is completed.</p>
         *
         * <p>If there was no value calculated and cached prior to the call of this
         * method, then one will be calculated.</p>
         *
         * @param queryOptions the query options
         * @throws NullPointerException if contexts is null
         */
        void recalculate(@NonNull QueryOptions queryOptions);

        /**
         * (Re)loads permission data for a given context.
         *
         * <p>Unlike {@link #recalculate(QueryOptions)}, this method immediately
         * invalidates any previous data values contained within the cache,
         * and then schedules a task to reload a new data instance to
         * replace the one which was invalidated.</p>
         *
         * <p>The invalidation happens immediately during the execution of this method.
         * The result of the re-computation encapsulated by the future.</p>
         *
         * <p>Subsequent calls to {@link #get(QueryOptions)} will block until
         * the result of this operation is complete.</p>
         *
         * <p>If there was no value calculated and cached prior to the call of this
         * method, then one will be calculated.</p>
         *
         * <p>This method returns a Future so users can optionally choose to wait
         * until the recalculation has been performed.</p>
         *
         * @param queryOptions the query options.
         * @return a future
         * @throws NullPointerException if contexts is null
         */
        @NonNull CompletableFuture<? extends T> reload(@NonNull QueryOptions queryOptions);

        /**
         * Recalculates data for all known contexts.
         *
         * <p>This method returns immediately. The recalculation is performed
         * asynchronously and applied to the cache in the background.</p>
         *
         * <p>The previous data instances will continue to be returned
         * by {@link #get(QueryOptions)} until the recalculation is completed.</p>
         */
        void recalculate();

        /**
         * Reloads permission data for all known contexts.
         *
         * <p>Unlike {@link #recalculate()}, this method immediately
         * invalidates all previous data values contained within the cache,
         * and then schedules a task to reload new data instances to
         * replace the ones which were invalidated.</p>
         *
         * <p>The invalidation happens immediately during the execution of this method.
         * The result of the re-computation encapsulated by the future.</p>
         *
         * <p>Subsequent calls to {@link #get(QueryOptions)} will block until
         * the result of this operation is complete.</p>
         *
         * <p>This method returns a Future so users can optionally choose to wait
         * until the recalculation has been performed.</p>
         *
         * @return a future
         */
        @NonNull CompletableFuture<Void> reload();

        /**
         * Invalidates any cached data instances mapped to the given context.
         *
         * @param queryOptions the queryOptions to invalidate for
         */
        void invalidate(@NonNull QueryOptions queryOptions);

        /**
         * Invalidates all cached data instances.
         */
        void invalidate();
    }

}
