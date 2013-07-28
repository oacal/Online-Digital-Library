package com.sjtu.onlinelibrary.impl;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.sjtu.onlinelibrary.DataAccessException;
import com.sjtu.onlinelibrary.EntityChangeListener;
import com.sjtu.onlinelibrary.MutableDataAccess;
import com.sjtu.onlinelibrary.Persistable;
import com.sjtu.onlinelibrary.entity.User;
import com.sjtu.onlinelibrary.util.MongoConfig;

import java.util.UUID;

/**
 * Mongo version of the registry
 */
public final class DataAccessMongoImpl implements MutableDataAccess {

    private final Datastore _ds;
    private final EntityChangeListenerManager _listeners = new EntityChangeListenerManager();

    public DataAccessMongoImpl(final Datastore ds) {
        _ds = ds;
    }

    public DataAccessMongoImpl(final MongoConfig mongoConfig) {
        this(mongoConfig.newMongoInstance(), mongoConfig.getDbName(), mongoConfig.getUserName(), mongoConfig.getPassword());
    }

    public DataAccessMongoImpl(final Mongo mongo, final String dbName, final String username, final String password) {
        final Morphia m = new Morphia();
        m.mapPackage(MutableDataAccess.class.getPackage().getName());
        _ds = m.createDatastore(mongo, dbName, username, password == null ? null : password.toCharArray());
        _ds.ensureIndexes(); //creates all defined with @Indexed
        _ds.ensureCaps(); //creates all collections for @Entity(cap=@CappedAt(...))
    }


    @Override
    public <T extends Persistable> Iterable<T> listAll(final Class<T> clazz) throws DataAccessException {
        try {
            return _ds.find(clazz);
        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
            throw new DataAccessException("Could not list " + clazz.getSimpleName() + " entities", e);
        }
    }

    @Override
    public <T extends Persistable> T findById(final Class<T> clazz, final String id) throws DataAccessException {
        try {
            return _ds.get(clazz, id);
        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
            throw new DataAccessException("Could not lookup entity " + clazz.getSimpleName() + " with id " + id, e);
        }
    }

    public User findServiceAdapterByServiceNameAndVersion(final String serviceName, final String serviceVersion) throws DataAccessException {
        try {
            return _ds.find(User.class).filter("name", serviceName).filter("version", serviceVersion).get();
        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
            throw new DataAccessException("Could not lookup ServiceAdapter with name  " + serviceName + " and version " + serviceVersion, e);
        }
    }

//    @Override
//    public Iterable<ProjectServiceAdapterConfig> findProjectServiceAdapterConfigsByServiceIdAndProjectId(final String projectId, final String serviceAdapterId) throws RegistryException {
//        try {
//            Query<ProjectServiceAdapterConfig> qry = _ds.find(ProjectServiceAdapterConfig.class);
//            if (serviceAdapterId != null) qry = qry.filter("serviceAdapterId", serviceAdapterId);
//            if (projectId != null) qry = qry.filter("projectId", projectId);
//            return qry;
//        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
//            throw new RegistryException("Could not lookup ProjectServiceAdapterConfig by service id " + serviceAdapterId + " and projectId" + projectId, e);
//        }
//    }

    @Override
    public boolean exists(final Class type, final String id) throws DataAccessException {
        try {
            return _ds.find(type, "_id", id).countAll() != 0;
        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
            throw new DataAccessException("Could determine existence of " + type.getName() + " with id " + id, e);
        }
    }

    @Override
    public void save(final Persistable p) throws DataAccessException {
        try {
            final boolean exists;
            if (p.getId() == null) {
                p.setId(UUID.randomUUID().toString());
                exists = false;
            } else {
                exists = exists(p.getClass(), p.getId());
            }
            p.validate(exists ? Persistable.ValidationOperation.UPDATE : Persistable.ValidationOperation.ADD, this);
            _ds.save(p);
            _listeners.fireItemSaved(p, !exists);
        } catch (DataAccessException re) {
            throw re;
        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
            throw new DataAccessException("Failed to save entity " + p, e);
        }
    }

    @Override
    public void delete(final Class<? extends Persistable> type, final String id) throws DataAccessException {
        try {
            final Persistable oldObject = findById(type, id);
            if (oldObject != null) {
                oldObject.validate(Persistable.ValidationOperation.DELETE, this);
                _ds.delete(type, id);
                _listeners.fireItemDeleted(oldObject);
            }
        } catch (DataAccessException re) {
            throw re;
        } catch (Exception e) { // for some reason mongo and morphia chose to throw only runtime exceptions - uhg!
            throw new DataAccessException("Failed to delete entity " + type.getSimpleName() + " with id " + id, e);
        }
    }

    @Override
    public void addListener(EntityChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
