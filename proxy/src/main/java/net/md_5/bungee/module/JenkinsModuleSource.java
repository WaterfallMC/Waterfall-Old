package net.md_5.bungee.module;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import lombok.Data;
import net.md_5.bungee.Util;

@Data
public class JenkinsModuleSource implements ModuleSource
{

    @Override
    public void retrieve(ModuleSpec module, ModuleVersion version)
    {
        System.out.println( "Attempting to Jenkins download module " + module.getName() + " v" + version.getBuild() );
        try
        {
            URL website = new URL( "https://ci.getwaterfall.xyz/guestAuth/app/rest/builds/buildType:Waterfall_Build,branch:master,number:" + version.getBuild() + "/artifacts/content/" + module.getName() + ".jar" );
            URLConnection con = website.openConnection();
            // 15 second timeout at various stages
            con.setConnectTimeout( 15000 );
            con.setReadTimeout( 15000 );
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");

            Files.copy(con.getInputStream(), module.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println( "Download complete" );
        } catch ( IOException ex )
        {
            System.out.println( "Failed to download: " + Util.exception( ex ) );
        }
    }
}
