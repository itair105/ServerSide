package common;

import java.io.BufferedWriter;
import java.io.ObjectOutputStream;

/**
 * Created by user on 12/13/2014.
 */
public interface Command {
    void doCommand(String param, ObjectOutputStream out);
}
