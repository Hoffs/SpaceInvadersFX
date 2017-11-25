package com.ignasm.spaceinvaders;

import com.ignasm.spaceinvaders.entities.Entity;
import javafx.scene.layout.Pane;

import java.util.Arrays;

/**
 * Ignas Maslinskas
 * 20153209
 * PRIf-15/1
 */
public class ShotPool extends ObjectPool<Entity> {
    private Pane window;
    private Entity dummyEntity;

    public ShotPool(Entity baseEntity, int size, Pane parent) {
        super(size);
        dummyEntity = baseEntity;
        window = parent;
    }

    @Override
    public Entity acquireObject() {
        Entity shot = getReleasedObjects().poll();

        if (shot == null && getObjectCount() < getPoolSize()) {
            shot = createObject();
        }

        if (shot != null) {
            getActiveObjects().add(shot);
            window.getChildren().add(shot);
        }

        return shot;
    }

    @Override
    public void releaseObject(Entity object) {
        if (getActiveObjects().contains(object)) {
            getActiveObjects().remove(object);
            window.getChildren().remove(object);
            getReleasedObjects().add(object);
        }
    }

    public void moveShots(int yChange) {
        getActiveObjects().forEach(entity -> entity.moveY(yChange));
        removeOutOfBounds();
    }

    private void removeOutOfBounds() {
        Entity[] toRemove =  getActiveObjects().stream()
                .filter(entity -> entity.isOutOfBounds(window))
                .toArray(Entity[]::new);
        Arrays.stream(toRemove).forEach(this::releaseObject);
    }

    @Override
    public Entity createObject() {
        return new Entity(dummyEntity.getImage(),
                dummyEntity.getEntityWidth(),
                dummyEntity.getEntityHeight(),
                dummyEntity.getAnimationDuration()
        );
    }
}
