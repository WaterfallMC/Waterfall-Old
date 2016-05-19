package net.md_5.bungee.api.chat;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TranslatableComponentTest
{

    @Test
    public void testMissingPlaceholdersAdded()
    {
        TranslatableComponent testComponent = new TranslatableComponent( "Test string with %s placeholders: %s", "2", "aoeu" );
        assertEquals( "Test string with 2 placeholders: aoeu", testComponent.toPlainText() );
        assertEquals( "§fTest string with §f2§f placeholders: §faoeu", testComponent.toLegacyText() );
    }

    @Test
    public void testDuplicateNullWithDoesntThrowException() {
        TranslatableComponent component = new TranslatableComponent("Test") {
            @Override
            public List<BaseComponent> getExtra() {
                return null;
            }
        };

        TranslatableComponent copy = new TranslatableComponent(component);
        // The fact that we don't throw an exception means it's working as intended.
    }

    @Test
    public void testEscapedPercentInPlainText()
    {
        TranslatableComponent testComponent = new TranslatableComponent( "Test string with %% sign" );
        assertEquals( "Test string with % sign", testComponent.toPlainText() );
        assertEquals( "§fTest string with §f%§f sign", testComponent.toLegacyText() );
    }
}
