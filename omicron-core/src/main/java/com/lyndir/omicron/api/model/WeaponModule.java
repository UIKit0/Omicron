package com.lyndir.omicron.api.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.lyndir.lhunath.opal.system.util.ObjectUtils;
import java.util.Random;
import java.util.Set;


public class WeaponModule extends Module {

    private static final Random RANDOM = new Random();
    private final int            firePower;
    private final int            variance;
    private final int            range;
    private final int            repeat;
    private final int            ammunitionLoad;
    private final Set<LevelType> supportedLayers;
    private       int            repeated;
    private       int            ammunition;

    protected WeaponModule(final ImmutableResourceCost resourceCost, final int firePower, final int variance, final int range,
                           final int repeat, final int ammunitionLoad, final Set<LevelType> supportedLayers) {
        super( resourceCost );

        this.firePower = firePower;
        this.variance = variance;
        this.range = range;
        this.repeat = repeat;
        this.ammunitionLoad = ammunitionLoad;
        this.supportedLayers = supportedLayers;

        ammunition = ammunitionLoad;
    }

    static Builder0 createWithStandardResourceCost() {
        return createWithExtraResourceCost( ResourceCost.immutable() );
    }

    static Builder0 createWithExtraResourceCost(final ImmutableResourceCost resourceCost) {
        return new Builder0( ModuleType.WEAPON.getStandardCost().add( resourceCost ) );
    }

    public int getFirePower() {

        return firePower;
    }

    public int getVariance() {

        return variance;
    }

    public int getRange() {

        return range;
    }

    public int getRepeat() {

        return repeat;
    }

    public int getAmmunitionLoad() {

        return ammunitionLoad;
    }

    public ImmutableSet<LevelType> getSupportedLayers() {

        return ImmutableSet.copyOf( supportedLayers );
    }

    void fireAt(final Player currentPlayer, final Tile target) {

        Preconditions.checkArgument( ObjectUtils.isEqual( currentPlayer, getGameObject().getOwner().orNull() ), //
                                     "Cannot fire: unit is not owned by player." );
        Preconditions.checkArgument( currentPlayer.canObserve( currentPlayer, target ), //
                                     "Cannot fire: target not observed." );
        Preconditions.checkArgument( getGameObject().getLocation().getPosition().distanceTo( target.getPosition() ) <= range, //
                                     "Cannot fire: target not in range." );
        Preconditions.checkState( repeated < repeat, //
                                  "Cannot fire: no repeats left." );
        Preconditions.checkState( ammunition > 0, //
                                  "Cannot fire: no ammunition left." );

        Optional<GameObject> targetGameObject = target.getContents();
        if (targetGameObject.isPresent())
            targetGameObject.get().onModule( ModuleType.BASE, 0 ).addDamage( firePower + RANDOM.nextInt( variance ) );

        ++repeated;
        --ammunition;
    }

    @Override
    protected void onReset() {
        repeated = 0;
    }

    @Override
    protected void onNewTurn() {
    }

    @Override
    public ModuleType<WeaponModule> getType() {
        return ModuleType.WEAPON;
    }

    @SuppressWarnings({ "ParameterHidesMemberVariable", "InnerClassFieldHidesOuterClassField" })
    static class Builder0 {

        private final ImmutableResourceCost resourceCost;

        private Builder0(final ImmutableResourceCost resourceCost) {
            this.resourceCost = resourceCost;
        }

        Builder1 firePower(final int firePower) {
            return new Builder1( firePower );
        }

        class Builder1 {

            private final int firePower;

            private Builder1(final int firePower) {
                this.firePower = firePower;
            }

            Builder2 armor(final int variance) {
                return new Builder2( variance );
            }

            class Builder2 {

                private final int variance;

                private Builder2(final int variance) {
                    this.variance = variance;
                }

                Builder3 range(final int range) {
                    return new Builder3( range );
                }

                class Builder3 {

                    private final int range;

                    private Builder3(final int range) {
                        this.range = range;
                    }

                    Builder4 repeat(final int repeat) {
                        return new Builder4( repeat );
                    }

                    class Builder4 {

                        private final int repeat;

                        private Builder4(final int repeat) {
                            this.repeat = repeat;
                        }

                        Builder5 ammunitionLoad(final int ammunitionLoad) {
                            return new Builder5( ammunitionLoad );
                        }

                        class Builder5 {

                            private final int ammunitionLoad;

                            private Builder5(final int ammunitionLoad) {
                                this.ammunitionLoad = ammunitionLoad;
                            }

                            WeaponModule supportedLayers(final Set<LevelType> supportedLayers) {
                                return new WeaponModule( resourceCost, firePower, variance, range, repeat, ammunitionLoad,
                                                         supportedLayers );
                            }
                        }
                    }
                }
            }
        }
    }
}
