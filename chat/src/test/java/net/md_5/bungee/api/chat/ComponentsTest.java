package net.md_5.bungee.api.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.junit.Assert;
import org.junit.Test;

public class ComponentsTest
{

    @Test
    public void testBasicComponent()
    {
        TextComponent textComponent = new TextComponent( "Hello world" );
        textComponent.setColor( ChatColor.RED );

        Assert.assertEquals( "Hello world", textComponent.toPlainText() );
        Assert.assertEquals( ChatColor.RED + "Hello world", textComponent.toLegacyText() );
    }

    @Test
    public void testFromLegacyConverter()
    {
        BaseComponent[] test1 = TextComponent.fromLegacyText( ChatColor.AQUA + "Aqua " + ChatColor.RED + ChatColor.BOLD + "RedBold" );

        Assert.assertEquals( "Aqua RedBold", BaseComponent.toPlainText( test1 ) );
        Assert.assertEquals( ChatColor.AQUA + "Aqua " + ChatColor.RED + ChatColor.BOLD + "RedBold", BaseComponent.toLegacyText( test1 ) );

        BaseComponent[] test2 = TextComponent.fromLegacyText( "Text http://spigotmc.org " + ChatColor.GREEN + "google.com/test" );

        Assert.assertEquals( "Text http://spigotmc.org google.com/test", BaseComponent.toPlainText( test2 ) );
        //The extra ChatColor.WHITEs are sometimes inserted when not needed but it doesn't change the result
        Assert.assertEquals( ChatColor.WHITE + "Text " + ChatColor.WHITE + "http://spigotmc.org" + ChatColor.WHITE
                + " " + ChatColor.GREEN + "google.com/test", BaseComponent.toLegacyText( test2 ) );

        ClickEvent url1 = test2[1].getClickEvent();
        Assert.assertNotNull( url1 );
        Assert.assertTrue( url1.getAction() == ClickEvent.Action.OPEN_URL );
        Assert.assertEquals( "http://spigotmc.org", url1.getValue() );

        ClickEvent url2 = test2[3].getClickEvent();
        Assert.assertNotNull( url2 );
        Assert.assertTrue( url2.getAction() == ClickEvent.Action.OPEN_URL );
        Assert.assertEquals( "http://google.com/test", url2.getValue() );

        BaseComponent[] test3 = TextComponent.fromLegacyText(ChatColor.WHITE + ChatColor.BOLD.toString() + ChatColor.ITALIC + ChatColor.UNDERLINE +
                ChatColor.STRIKETHROUGH + ChatColor.MAGIC + "Hello world");

        Assert.assertEquals(ChatColor.WHITE, test3[0].getColor());
        Assert.assertTrue(test3[0].isBold());
        Assert.assertTrue(test3[0].isItalic());
        Assert.assertTrue(test3[0].isUnderlined());
        Assert.assertTrue(test3[0].isStrikethrough());
        Assert.assertTrue(test3[0].isObfuscated());
    }

    @Test
    public void testToLegacyConverter()
    {
        TextComponent test1 = new TextComponent("Hello world");
        test1.setBold(true);
        test1.setItalic(true);
        test1.setUnderlined(true);
        test1.setStrikethrough(true);
        test1.setObfuscated(true);
        Assert.assertEquals( ChatColor.WHITE + ChatColor.BOLD.toString() + ChatColor.ITALIC + ChatColor.UNDERLINE +
                ChatColor.STRIKETHROUGH + ChatColor.MAGIC + "Hello world", test1.toLegacyText() );
    }

    @Test
    public void testTranslateComponent()
    {
        TranslatableComponent item = new TranslatableComponent( "item.swordGold.name" );
        item.setColor( ChatColor.AQUA );
        TranslatableComponent translatableComponent = new TranslatableComponent( "commands.give.success",
                item, "5",
                "thinkofdeath" );

        Assert.assertEquals( "Given Golden Sword * 5 to thinkofdeath", translatableComponent.toPlainText() );
        Assert.assertEquals( ChatColor.WHITE + "Given " + ChatColor.AQUA + "Golden Sword" + ChatColor.WHITE
                + " * " + ChatColor.WHITE + "5" + ChatColor.WHITE + " to " + ChatColor.WHITE + "thinkofdeath",
                translatableComponent.toLegacyText() );

        TranslatableComponent positional = new TranslatableComponent( "book.pageIndicator", "5", "50" );

        Assert.assertEquals( "Page 5 of 50", positional.toPlainText() );
        Assert.assertEquals( ChatColor.WHITE + "Page " + ChatColor.WHITE + "5" + ChatColor.WHITE + " of " + ChatColor.WHITE + "50", positional.toLegacyText() );
    }

    @Test
    public void testBuilder()
    {
        BaseComponent[] components = new ComponentBuilder( "Hello " ).color( ChatColor.RED ).
                append( "World" ).bold( true ).color( ChatColor.BLUE ).
                append( "!" ).color( ChatColor.YELLOW ).create();

        Assert.assertEquals( "Hello World!", BaseComponent.toPlainText( components ) );
        Assert.assertEquals( ChatColor.RED + "Hello " + ChatColor.BLUE + ChatColor.BOLD
                + "World" + ChatColor.YELLOW + ChatColor.BOLD + "!", BaseComponent.toLegacyText( components ) );
    }

    @Test
    public void testBuilderDuplicate()
    {
        ComponentBuilder builder1 = new ComponentBuilder( "Hello " ).color( ChatColor.RED ).
                append( "World" ).bold( true ).color( ChatColor.BLUE ).
                append( "!" ).color( ChatColor.YELLOW );
        ComponentBuilder builder2 = new ComponentBuilder( builder1 );

        // TextComponent doesn't implement proper equals() and hashCode().
        // Until then, this will have to do...
        BaseComponent[] components1 = builder1.create();
        BaseComponent[] components2 = builder2.create();

        Assert.assertEquals( "Hello World!", BaseComponent.toPlainText( components1 ) );
        Assert.assertEquals( ChatColor.RED + "Hello " + ChatColor.BLUE + ChatColor.BOLD
                + "World" + ChatColor.YELLOW + ChatColor.BOLD + "!", BaseComponent.toLegacyText( components1 ) );

        Assert.assertEquals( "Hello World!", BaseComponent.toPlainText( components2 ) );
        Assert.assertEquals( ChatColor.RED + "Hello " + ChatColor.BLUE + ChatColor.BOLD
                + "World" + ChatColor.YELLOW + ChatColor.BOLD + "!", BaseComponent.toLegacyText( components2 ) );
    }

    @Test
    public void testBuilderReset()
    {
        BaseComponent[] components = new ComponentBuilder( "Hello " ).color(ChatColor.RED)
                .append("World").reset().create();

        Assert.assertEquals( components[0].getColor(), ChatColor.RED );
        Assert.assertEquals( components[1].getColor(), ChatColor.WHITE );
    }

    @Test
    public void testBuilderFormatRetention()
    {
        BaseComponent[] noneRetention = new ComponentBuilder( "Hello " ).color( ChatColor.RED )
                .append( "World", ComponentBuilder.FormatRetention.NONE ).create();

        Assert.assertEquals( noneRetention[0].getColor(), ChatColor.RED );
        Assert.assertEquals( noneRetention[1].getColor(), ChatColor.WHITE );

        HoverEvent testEvent = new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "test" ).create() );

        BaseComponent[] formattingRetention = new ComponentBuilder( "Hello " ).color( ChatColor.RED )
                .event( testEvent ).append( "World", ComponentBuilder.FormatRetention.FORMATTING ).create();

        Assert.assertEquals( formattingRetention[0].getColor(), ChatColor.RED );
        Assert.assertEquals( formattingRetention[0].getHoverEvent(), testEvent );
        Assert.assertEquals( formattingRetention[1].getColor(), ChatColor.RED );
        Assert.assertNull( formattingRetention[1].getHoverEvent() );

        ClickEvent testClickEvent = new ClickEvent( ClickEvent.Action.OPEN_URL, "http://www.example.com" );

        BaseComponent[] eventRetention = new ComponentBuilder( "Hello " ).color( ChatColor.RED )
                .event( testEvent ).event(testClickEvent).append("World", ComponentBuilder.FormatRetention.EVENTS).create();

        Assert.assertEquals( eventRetention[0].getColor(), ChatColor.RED );
        Assert.assertEquals( eventRetention[0].getHoverEvent(), testEvent );
        Assert.assertEquals( eventRetention[0].getClickEvent(), testClickEvent );
        Assert.assertEquals( eventRetention[1].getColor(), ChatColor.WHITE );
        Assert.assertEquals( eventRetention[1].getHoverEvent(), testEvent );
        Assert.assertEquals( eventRetention[1].getClickEvent(), testClickEvent );
    }

    @Test
    public void testBuilderSpecialFormatting()
    {
        BaseComponent[] components = new ComponentBuilder( "Hello " )
                .bold(true).underlined(true).italic(true).strikethrough(true).obfuscated(true)
                .append("World").underlined(false).strikethrough(false).create();

        Assert.assertTrue( components[0].isBold() );
        Assert.assertTrue( components[0].isUnderlined() );
        Assert.assertTrue( components[0].isItalic() );
        Assert.assertTrue( components[0].isStrikethrough() );
        Assert.assertTrue( components[0].isObfuscated() );

        Assert.assertTrue( components[1].isBold() );
        Assert.assertFalse( components[1].isUnderlined() );
        Assert.assertTrue( components[1].isItalic() );
        Assert.assertFalse( components[1].isStrikethrough() );
        Assert.assertTrue( components[1].isObfuscated() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoopSimple()
    {
        TextComponent component = new TextComponent( "Testing" );
        component.addExtra( component );
        ComponentSerializer.toString( component );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoopComplex()
    {
        TextComponent a = new TextComponent( "A" );
        TextComponent b = new TextComponent( "B" );
        b.setColor( ChatColor.AQUA );
        TextComponent c = new TextComponent( "C" );
        c.setColor( ChatColor.RED );
        a.addExtra( b );
        b.addExtra( c );
        c.addExtra( a );
        ComponentSerializer.toString( a );
    }

    @Test
    public void testRepeated()
    {
        TextComponent a = new TextComponent( "A" );
        TextComponent b = new TextComponent( "B" );
        b.setColor( ChatColor.AQUA );
        a.addExtra( b );
        a.addExtra( b );
        ComponentSerializer.toString( a );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatedError()
    {
        TextComponent a = new TextComponent( "A" );
        TextComponent b = new TextComponent( "B" );
        b.setColor( ChatColor.AQUA );
        TextComponent c = new TextComponent( "C" );
        c.setColor( ChatColor.RED );
        a.addExtra( b );
        a.addExtra( c );
        c.addExtra( a );
        a.addExtra( b );
        ComponentSerializer.toString( a );
    }
}
