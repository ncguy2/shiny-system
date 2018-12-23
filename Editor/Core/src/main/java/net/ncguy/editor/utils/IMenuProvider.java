package net.ncguy.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.PopupMenu;
import net.ncguy.foundation.ui.MenuBuilder;

import java.util.Optional;

public interface IMenuProvider {

    BuildDirection defaultBuildDirection = BuildDirection.ParentAbove_FocusedTop;

    public static void addMenuListener(Actor actor) {
        addMenuListener(defaultBuildDirection, actor);
    }

    public static void addMenuListener(BuildDirection direction, Actor actor) {
        Gdx.app.postRunnable(() -> {
            IMenuProvider
                    .buildFullMenu(direction, actor)
                    .ifPresent(menu -> {
                        actor.addListener(new ClickListener(Input.Buttons.RIGHT) {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                boolean handled = event.isHandled();
                                if (!handled) {
                                    menu.setUserObject(this);
                                    menu.showMenu(actor.getStage(), event.getStageX(), event.getStageY());
                                    event.handle();
                                }
                            }
                        });
                    });
        });
    }

    public static Optional<PopupMenu> buildFullMenu(BuildDirection direction, Actor actor) {
        MenuBuilder.MenuNode b = MenuBuilder.Begin();
        buildFullMenu(direction, actor, actor, b);
        return b.Build();
    }

    public static void buildFullMenu(BuildDirection direction, Actor focused, Actor actor, MenuBuilder.MenuNode node) {
        Group parent = actor.getParent();
        switch (direction) {
            case ParentAbove:
                if (parent != null) {
                    buildFullMenu(direction, focused, parent, node);
                }
                if (actor instanceof IMenuProvider) {
                    ((IMenuProvider) actor).provideMenu(node);
                }
                break;
            case ParentBelow:
                if (actor instanceof IMenuProvider) {
                    ((IMenuProvider) actor).provideMenu(node);
                }
                if (parent != null) {
                    buildFullMenu(direction, focused, parent, node);
                }
                break;
            case ParentAbove_FocusedTop:
                if (actor == focused) {
                    if (actor instanceof IMenuProvider) {
                        ((IMenuProvider) actor).provideMenu(node);
                    }
                }
                if (parent != null) {
                    buildFullMenu(direction, focused, parent, node);
                }
                if (actor != focused) {
                    if (actor instanceof IMenuProvider) {
                        ((IMenuProvider) actor).provideMenu(node);
                    }
                }
                break;

        }
    }

    void provideMenu(MenuBuilder.MenuNode node);

    enum BuildDirection {
        ParentAbove,
        ParentBelow,
        ParentAbove_FocusedTop
    }

}
