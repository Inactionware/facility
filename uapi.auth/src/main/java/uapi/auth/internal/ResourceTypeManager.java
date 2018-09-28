/*
 *  Copyright (C) 2017. The UAPI Authors
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at the LICENSE file.
 *
 *  You must gained the permission from the authors if you want to
 *  use the project into a commercial product
 */

package uapi.auth.internal;

import uapi.GeneralException;
import uapi.auth.*;
import uapi.common.ArgumentChecker;
import uapi.service.IServiceLifecycle;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Optional;
import uapi.service.annotation.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(IResourceTypeManager.class)
public class ResourceTypeManager implements IResourceTypeManager, IServiceLifecycle {

    private final Map<String, IResourceType> _resTypes = new ConcurrentHashMap<>();

    private final Map<String, IResourceLoader> _resLoaders = new ConcurrentHashMap<>();

    @Inject
    @Optional
    @Override
    public void register(IResourceType resourceType) {
        ArgumentChecker.required(resourceType, "resourceType");
        String resName = resourceType.name();
        if (this._resTypes.containsKey(resName)) {
            throw AuthenticationException.builder()
                    .errorCode(AuthenticationErrors.DUPLICATED_RESOURCE_TYPE)
                    .variables(new AuthenticationErrors.DuplicatedResourceType()
                            .resourceTypeName(resName))
                    .build();
        }
        this._resTypes.put(resName, resourceType);
    }

    @Inject
    @Optional
    @Override
    public void register(IResourceLoader loader) {
        ArgumentChecker.required(loader, "loader");
        IResourceLoader existingLoader = this._resLoaders.get(loader.resourceTypeName());
        if (existingLoader != null) {
            throw AuthenticationException.builder()
                    .errorCode(AuthenticationErrors.DUPLICATED_RESOURCE_LOADER)
                    .variables(new AuthenticationErrors.DuplicatedResourceLoader()
                            .resourceTypeName(loader.resourceTypeName()))
                    .build();
        }
        this._resLoaders.put(loader.resourceTypeName(), loader);
    }

    @Override
    public IResourceType register(String resourceTypeName) {
        ArgumentChecker.required(resourceTypeName, "resourceTypeName");
        IResourceType resType = this._resTypes.get(resourceTypeName);
        if (resType != null) {
            throw AuthenticationException.builder()
                    .errorCode(AuthenticationErrors.DUPLICATED_RESOURCE_TYPE)
                    .variables(new AuthenticationErrors.DuplicatedResourceType()
                            .resourceTypeName(resourceTypeName))
                    .build();
        }
        resType = new ResourceType(resourceTypeName);
        this._resTypes.put(resourceTypeName, resType);
        return resType;
    }

    @Override
    public IResourceType findResourceType(String resourceTypeName) {
        ArgumentChecker.required(resourceTypeName, "resourceTypeName");
        return this._resTypes.get(resourceTypeName);
    }

    @Override
    public void onDependencyInject(String serviceId, Object service) {
        ArgumentChecker.required(service, "service");
        if (service instanceof IResourceType) {
            IResourceType resType = (IResourceType) service;
            this._resTypes.put(resType.name(), resType);
        } else if (service instanceof IResourceLoader) {
            IResourceLoader resLoader = (IResourceLoader) service;
            this._resLoaders.put(resLoader.resourceTypeName(), resLoader);
        } else {
            throw new GeneralException(
                    "Inject unsupported service - id: {}, service: {}",
                    serviceId, service.getClass().getName());
        }
    }
}