package uapi.net.internal;

import uapi.GeneralException;
import uapi.InvalidArgumentException;
import uapi.common.ArgumentChecker;
import uapi.common.Guarder;
import uapi.net.INetListenerInitializer;
import uapi.net.INetListenerMeta;
import uapi.net.INetListenerRegistry;
import uapi.service.IServiceLifecycle;
import uapi.service.annotation.Inject;
import uapi.service.annotation.Optional;
import uapi.service.annotation.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service(INetListenerRegistry.class)
public class NetListenerRegistry implements INetListenerRegistry, IServiceLifecycle {

    @Inject
    @Optional
    protected Map<String, INetListenerMeta> _listenerMetas = new HashMap<>();

    private final Lock _lock = new ReentrantLock();

    @Override
    public void register(final INetListenerMeta netListenerMeta) {
        ArgumentChecker.required(netListenerMeta, "netListenerMeta");
        Guarder.by(this._lock).run(() -> {
            if (this._listenerMetas.containsKey(netListenerMeta.getId())) {
                throw new InvalidArgumentException(
                        "The id of net listener meta exist in the repo: {}", netListenerMeta.getId());
            }
            this._listenerMetas.put(netListenerMeta.getId(), netListenerMeta);
        });
    }

    @Override
    public void unregister(final String type) {
        ArgumentChecker.required(type, "type");
        Guarder.by(this._lock).run(() -> this._listenerMetas.remove(type));
    }

    @Override
    public INetListenerInitializer getInitializer(String type) {
        ArgumentChecker.required(type, "type");
        INetListenerMeta listenerMeta = Guarder.by(this._lock).runForResult(() -> this._listenerMetas.get(type));
        return listenerMeta == null ? null : listenerMeta.newInitializer();
    }

    @Override
    public void onActivate() {
        // do nothing
    }

    @Override
    public void onDeactivate() {
        // do nothing
    }

    @Override
    public void onDependencyInject(String serviceId, Object service) {
        if (service instanceof INetListenerMeta) {
            register((INetListenerMeta) service);
        } else {
            throw new GeneralException("Unsupported injected service type - {} with service id - {}",
                    service.getClass().getCanonicalName(), serviceId);
        }
    }
}
