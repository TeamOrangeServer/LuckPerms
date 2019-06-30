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

package me.lucko.luckperms.api;

import me.lucko.luckperms.api.actionlog.ActionLogger;
import me.lucko.luckperms.api.context.ContextCalculator;
import me.lucko.luckperms.api.context.ContextManager;
import me.lucko.luckperms.api.context.ContextSetFactory;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.messaging.MessagingService;
import me.lucko.luckperms.api.messenger.MessengerProvider;
import me.lucko.luckperms.api.metastacking.MetaStackDefinition;
import me.lucko.luckperms.api.metastacking.MetaStackElement;
import me.lucko.luckperms.api.metastacking.MetaStackFactory;
import me.lucko.luckperms.api.model.group.Group;
import me.lucko.luckperms.api.model.group.GroupManager;
import me.lucko.luckperms.api.model.user.User;
import me.lucko.luckperms.api.model.user.UserManager;
import me.lucko.luckperms.api.node.NodeBuilderRegistry;
import me.lucko.luckperms.api.platform.PlatformInfo;
import me.lucko.luckperms.api.track.Track;
import me.lucko.luckperms.api.track.TrackManager;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The LuckPerms API.
 *
 * <p>This interface is the base of the entire API package. All API functions
 * are accessed via this interface.</p>
 *
 * <p>An instance can be obtained via {@link LuckPermsProvider#get()}, or the platforms
 * Services Manager.</p>
 */
public interface LuckPerms {

    /**
     * TODO
     *
     * @return
     */
    String getServerName();

    /**
     * TODO
     *
     * @return
     */
    ContextSetFactory getContextSetFactory();

    /**
     * Gets information about the platform LuckPerms is running on.
     *
     * @return the platform info
     */
    @NonNull PlatformInfo getPlatformInfo();

    /**
     * Gets the {@link UserManager}, responsible for managing
     * {@link User} instances.
     *
     * <p>This manager can be used to retrieve instances of {@link User} by uuid
     * or name, or query all loaded users.</p>
     *
     * @return the user manager
     */
    @NonNull UserManager getUserManager();

    /**
     * Gets the {@link GroupManager}, responsible for managing
     * {@link Group} instances.
     *
     * <p>This manager can be used to retrieve instances of {@link Group} by
     * name, or query all loaded groups.</p>
     *
     * @return the group manager
     */
    @NonNull GroupManager getGroupManager();

    /**
     * Gets the {@link TrackManager}, responsible for managing
     * {@link Track} instances.
     *
     * <p>This manager can be used to retrieve instances of {@link Track} by
     * name, or query all loaded tracks.</p>
     *
     * @return the track manager
     */
    @NonNull TrackManager getTrackManager();

    /**
     * Schedules the execution of an update task, and returns an encapsulation
     * of the task as a {@link CompletableFuture}.
     *
     * <p>The exact actions performed in an update task remains an
     * implementation detail of the plugin, however, as a minimum, it is
     * expected to perform a full reload of user, group and track data, and
     * ensure that any changes are fully applied and propagated.</p>
     *
     * @return a future
     */
    @NonNull CompletableFuture<Void> runUpdateTask();

    /**
     * Gets the {@link EventBus}, used for subscribing to internal LuckPerms
     * events.
     *
     * @return the event bus
     */
    @NonNull EventBus getEventBus();

    /**
     * Gets the {@link MessagingService}, if present.
     *
     * <p>The MessagingService is used to dispatch updates throughout a network
     * of servers running the plugin.</p>
     *
     * <p>Not all instances of LuckPerms will have a messaging service setup and
     * configured, but it is recommended that all users of the API account for
     * and make use of this.</p>
     *
     * @return the messaging service instance, if present.
     */
    @NonNull Optional<MessagingService> getMessagingService();

    /**
     * Registers a {@link MessengerProvider} for use by the platform.
     *
     * <p>Note that the mere action of registering a provider doesn't
     * necessarily mean that it will be used.</p>
     *
     * @param messengerProvider the messenger provider.
     */
    void registerMessengerProvider(@NonNull MessengerProvider messengerProvider);

    /**
     * Gets the {@link ActionLogger}.
     *
     * <p>The action logger is responsible for saving and broadcasting defined
     * actions occurring on the platform.</p>
     *
     * @return the action logger
     */
    @NonNull ActionLogger getActionLogger();

    /**
     * Gets the {@link ContextManager}.
     *
     * <p>The context manager manages {@link ContextCalculator}s, and calculates
     * applicable contexts for a given type.</p>
     *
     * @return the context manager
     */
    @NonNull ContextManager getContextManager();

    /**
     * Gets a {@link Collection} of all known permission strings.
     *
     * @return a collection of the known permissions
     */
    @NonNull Collection<String> getKnownPermissions();

    /**
     * Gets the {@link NodeBuilderRegistry}.
     *
     * @return the node builder registry
     */
    @NonNull NodeBuilderRegistry getNodeBuilderRegistry();

    /**
     * Gets the {@link MetaStackFactory}.
     *
     * <p>The metastack factory provides methods for retrieving
     * {@link MetaStackElement}s and constructing
     * {@link MetaStackDefinition}s.</p>
     *
     * @return the meta stack factory
     */
    @NonNull MetaStackFactory getMetaStackFactory();

}
