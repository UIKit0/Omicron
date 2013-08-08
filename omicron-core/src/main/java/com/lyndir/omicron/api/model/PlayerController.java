package com.lyndir.omicron.api.model;

import com.google.common.base.*;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.lyndir.lhunath.opal.system.util.ObjectUtils;
import javax.annotation.Nonnull;


public class PlayerController implements GameObserver {

    private final Player         player;
    private       GameController gameController;

    public PlayerController(final Player player) {

        this.player = player;
    }

    @Nonnull
    @Override
    public Optional<Player> getOwner() {

        return Optional.of( player );
    }

    public void setGameController(final GameController gameController) {
        Preconditions.checkState( this.gameController == null, "This player has already been added to a game!" );
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return Preconditions.checkNotNull( gameController, "This player has not yet been added to a game!" );
    }

    @Override
    public boolean canObserve(@Nonnull final Player currentPlayer, @Nonnull final Tile location) {

        return FluentIterable.from( player.getObjects() ).anyMatch( new Predicate<GameObject>() {
            @Override
            public boolean apply(final GameObject input) {

                return input.canObserve( currentPlayer, location );
            }
        } );
    }

    @Nonnull
    @Override
    public Iterable<Tile> listObservableTiles(@Nonnull final Player currentPlayer) {

        return FluentIterable.from( player.getObjects() ).transformAndConcat( new Function<GameObject, Iterable<? extends Tile>>() {
            @Override
            public Iterable<? extends Tile> apply(final GameObject input) {

                return input.listObservableTiles( currentPlayer );
            }
        } );
    }

    /**
     * Iterate the objects of this player that the given observer can observe.
     *
     * @param observer The observer that we want to observe the player's objects with.
     *
     * @return An iterable of game objects owned by this controller's player.
     */
    public Iterable<GameObject> iterateObservableObjects(final GameObserver observer) {

        return FluentIterable.from( player.getObjects() ).filter( new Predicate<GameObject>() {
            @Override
            public boolean apply(final GameObject input) {

                Optional<Player> owner = observer.getOwner();
                if (owner.isPresent())
                    return observer.canObserve( owner.get(), input.getLocation() );

                return observer.equals( input );
            }
        } );
    }

    public Optional<GameObject> getObject(final Player currentPlayer, final int objectId) {

        GameObject object = player.getObject( objectId );

        // If the object cannot be observed by the current player, treat it as absent.
        if (object != null && !currentPlayer.canObserve( currentPlayer, object.getLocation() ))
            return Optional.absent();

        return Optional.fromNullable( object );
    }

    public int newObjectID() {

        return player.nextObjectID();
    }

    void addObject(final GameObject gameObject) {

        Preconditions.checkState( ObjectUtils.isEqual( player, gameObject.getOwner().orNull() ),
                                  "Cannot add object to this player: belongs to another player." );
        player.addObject( gameObject );
    }

    public void onReset() {

        for (final GameObject gameObject : ImmutableList.copyOf( player.getObjects() ))
            gameObject.getController().onReset();
    }

    public void onNewTurn() {

        for (final GameObject gameObject : ImmutableList.copyOf( player.getObjects() ))
            gameObject.getController().onNewTurn();

        if (player.isKeyLess())
            gameController.setReady( player );
    }
}