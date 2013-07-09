package com.lyndir.omnicron.cli;

import com.lyndir.omnicron.api.model.Game;
import java.util.Iterator;


/**
 * <i>10 07, 2012</i>
 *
 * @author lhunath
 */
@CommandGroup(name = "build", abbr = "b", desc = "Build a new game object.")
public class BuildCommand extends Command {

    @SubCommand(abbr = "g", desc = "Build a new game of Omnicron")
    public void game(final OmnicronCLI omnicron, final Iterator<String> tokens) {

        omnicron.getBuilders().setGameBuilder( Game.builder() );
        inf( "Building a new game of Omnicron.  Configure properties with 'set'/'add'/'rm' commands, use 'create' when done." );
    }
}
