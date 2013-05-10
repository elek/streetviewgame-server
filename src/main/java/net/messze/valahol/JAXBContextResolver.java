package net.messze.valahol;

import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import net.messze.valahol.data.Puzzle;
import net.messze.valahol.data.User;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

@Provider
@Singleton
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext context;
    private Class[] types = {User.class, Puzzle.class};

    public JAXBContextResolver() throws Exception {
        this.context =
                new JSONJAXBContext(JSONConfiguration.natural().humanReadableFormatting(true).build(), types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        System.out.println("resolve " + objectType);
        for (Class type : types) {
            if (type == objectType) {
                return context;
            }
        }
        return null;
    }


}
