package net.dialingspoon.questbind.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dialingspoon.questbind.Questbind;
import net.minecraft.client.option.KeyBinding;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KeyBindUtil {
    public ArrayList<ButtonBind> binds = new ArrayList<>();
    public byte read = 0;
    static File oculus_defaults = new File("openvr/input/oculus_defaults.json");
    static File customactionsets = new File("customactionsets.txt");
    static File actualSave = new File("openvr/input/keybinds.txt");
    JsonObject MainJson = new JsonObject();

    public void loadKeybinds() {
        try {
            readsave();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //create the default json format around the binds
        Gson gson = new Gson();
        MainJson = gson.fromJson(MainString, JsonObject.class);
        //add the binds, to the json and add actionsets
        StringBuilder builder = new StringBuilder();
        binds.forEach(bind -> compileKeybind(bind, builder));
        String actionsets = builder.toString();
        //replace the default binds with the new ones
        gson = new GsonBuilder().setPrettyPrinting().create();
        replaceFileData(oculus_defaults, gson.toJson(MainJson));
        replaceFileData(customactionsets, actionsets);
    }

    public void readsave() throws IOException {
        //add all the saved buttonbinds, unless there is no save then use defaults
        if (actualSave.exists()) {
            Scanner scanner = new Scanner(actualSave);
            if (scanner.hasNext()) {
                BufferedReader reader = new BufferedReader(new FileReader(actualSave));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        binds.add(new ButtonBind(parts[0], parts[1]));
                    }else if (parts.length == 6) {
                        Map<String, String> map = new HashMap<>();
                        if (!parts[2].isBlank()) {
                            String[] parameters = parts[2].split(",");
                            for (String parameter : parameters) {
                                String[] parameterParts = parameter.split("=");
                                map.put(parameterParts[0], parameterParts[1]);
                            }
                        }
                        binds.add(new ButtonBind(parts[0], parts[1], map, parts[3], parts[4], parts[5]));
                    }
                }
                reader.close();
            }else {
                binds = DEFAULTBINDS;
            }
        } else {
            binds = DEFAULTBINDS;
        }
        read = 2;
        KeyBinding.updateKeysByCode();
        read = 0;
    }

    public void compileKeybind(ButtonBind buttonBind, StringBuilder builder){
        if (buttonBind.button.equals("null")) return;
        JsonArray sources = MainJson
                .getAsJsonObject("bindings")
                .getAsJsonObject("/actions/" + buttonBind.path)
                .getAsJsonArray("sources");
        sources.add(buttonBind.asJson());
        if (!DEFAULTBINDS.contains(buttonBind))
            builder.append("\n")
                    .append(buttonBind.output)
                    .append(":")
                    .append(buttonBind.path);
    }

    public void actualSave() {
        //update bindings list from screen
        read = 1;
        KeyBinding.updateKeysByCode();
        read = 0;
        //format bindings and add to actual save file
        StringBuilder builder = new StringBuilder();
        binds.forEach(bind -> formatBind(bind, builder));
        String save = builder.toString();
        replaceFileData(actualSave, save);
    }

    private void formatBind(ButtonBind buttonBind, StringBuilder builder) {
        if (buttonBind.isDefault()) {
            builder.append("\n")
                    .append(buttonBind.output)
                    .append(";")
                    .append(buttonBind.button);
        } else {
            String parameters = buttonBind.parameters.toString();
            String trimmed = parameters.substring(1, parameters.length() - 1);
            String result = trimmed.replaceAll(",\\s+", ",");
            builder.append("\n")
                    .append(buttonBind.sensoryType)
                    .append(";")
                    .append(buttonBind.output)
                    .append(";")
                    .append(result)
                    .append(";")
                    .append(buttonBind.mode)
                    .append(";")
                    .append(buttonBind.button)
                    .append(";")
                    .append(buttonBind.path);
        }
    }

    public static void replaceFileData(File file, String data) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            Questbind.LOGGER.error("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //bindings json without keys (except joysticks)
    String MainString = """
                {
                   "action_manifest_version" : 0,
                   "alias_info" : {},
                   "app_key" : "org.jrbudda.vivecraft.steamvrinput",
                   "bindings" : {
                      "/actions/contextual" : {
                         "chords" : [
                            {
                               "inputs" : [
                                  [ "/user/hand/left/input/grip", "click" ],
                                  [ "/user/hand/right/input/grip", "click" ]
                               ],
                               "output" : "/actions/contextual/in/vivecraft.key.climbeyjump"
                            }
                         ],
                         "sources" : []
                      },
                      "/actions/global" : {
                         "haptics" : [
                            {
                               "output" : "/actions/global/out/lefthaptic",
                               "path" : "/user/hand/left/output/haptic"
                            },
                            {
                               "output" : "/actions/global/out/righthaptic",
                               "path" : "/user/hand/right/output/haptic"
                            }
                         ],
                         "poses" : [
                            {
                               "output" : "/actions/global/in/lefthand",
                               "path" : "/user/hand/left/pose/raw"
                            },
                            {
                               "output" : "/actions/global/in/righthand",
                               "path" : "/user/hand/right/pose/raw"
                            }
                         ],
                         "sources" : []
                      },
                      "/actions/gui" : {
                         "sources" : [
                            {
                               "inputs" : {
                                  "scroll" : {
                                     "output" : "/actions/gui/in/vivecraft.key.guiscrollaxis"
                                  }
                               },
                               "mode" : "scroll",
                               "parameters" : {
                                  "scroll_mode" : "discrete"
                               },
                               "path" : "/user/hand/right/input/joystick"
                            }
                         ]
                      },
                      "/actions/ingame" : {
                         "sources" : [
                            {
                               "inputs" : {
                                  "position" : {
                                     "output" : "/actions/ingame/in/vivecraft.key.freemovestrafe"
                                  }
                               },
                               "mode" : "joystick",
                               "path" : "/user/hand/left/input/joystick"
                            },
                            {
                               "inputs" : {
                                  "position" : {
                                     "output" : "/actions/ingame/in/vivecraft.key.rotateaxis"
                                  }
                               },
                               "mode" : "joystick",
                               "path" : "/user/hand/right/input/joystick"
                            }
                         ]
                      },
                      "/actions/keyboard" : {
                         "sources" : []
                      }
                   },
                   "category" : "steamvr_input",
                   "controller_type" : "oculus_touch",
                   "description" : "",
                   "name" : "Oculus Touch Defaults",
                   "options" : {},
                   "simulated_actions" : []
                }
                """;

    private static final HashMap<String, String> tempMap = new HashMap<>();
    static {
        tempMap.put("haptic_amplitude", "0");
    }
    public static ArrayList<ButtonBind> DEFAULTBINDS = new ArrayList<>(java.util.Arrays.asList(
            new ButtonBind("click", "vivecraft.key.climbeyGrab", tempMap, "button", "left/input/trigger", "contextual"),
            new ButtonBind("", "", new HashMap<>(), "button", "left/input/grip", "contextual"),
            new ButtonBind("", "", new HashMap<>(), "button", "right/input/grip", "contextual"),
            new ButtonBind("click", "vivecraft.key.climbeyGrab", tempMap, "button", "right/input/trigger", "contextual"),
            new ButtonBind("click", "vivecraft.key.climbeyGrab", new HashMap<>(), "button", "left/input/grip", "contextual"),
            new ButtonBind("click", "vivecraft.key.climbeyGrab", new HashMap<>(), "button", "right/input/grip", "contextual"),
            new ButtonBind("click", "vivecraft.key.vrInteract", new HashMap<>(), "button", "left/input/grip", "contextual"),
            new ButtonBind("click", "vivecraft.key.vrInteract", new HashMap<>(), "button", "right/input/grip", "contextual"),
            new ButtonBind("click", "vivecraft.key.vrInteract", tempMap, "button", "left/input/trigger", "contextual"),
            new ButtonBind("click", "vivecraft.key.vrInteract", tempMap, "button", "right/input/trigger", "contextual"),
            new ButtonBind("click", "vivecraft.key.ingameMenuButton", new HashMap<>(), "button", "left/input/application_menu", "global"),
            new ButtonBind("click", "key.inventory", new HashMap<>(), "button", "left/input/y", "global"),
            new ButtonBind("click", "vivecraft.key.guiShift", new HashMap<>(), "button", "left/input/grip", "gui"),
            new ButtonBind("click", "vivecraft.key.guiMiddleClick", new HashMap<>(), "button", "right/input/grip", "gui"),
            new ButtonBind("click", "vivecraft.key.guiLeftClick", new HashMap<>(), "button", "right/input/trigger", "gui"),
            new ButtonBind("click", "vivecraft.key.guiRightClick", new HashMap<>(), "button", "right/input/a", "gui"),
            //! Joystick?
            new ButtonBind("click", "vivecraft.key.toggleKeyboard", new HashMap<>(), "button", "null", "global"),
            new ButtonBind("vivecraft.key.hotbarPrev", "left/input/grip"),
            new ButtonBind("vivecraft.key.hotbarNext", "right/input/grip"),
            new ButtonBind("key.attack", "right/input/trigger"),
            new ButtonBind("vivecraft.key.teleport", "left/input/x"),
            new ButtonBind("vivecraft.key.radialMenu", "right/input/b"),
            new ButtonBind("key.use", "left/input/trigger"),
            new ButtonBind("key.jump", "right/input/a"),
            new ButtonBind("click", "key.sneak", new HashMap<>(), "toggle_button", "right/input/joystick", "ingame"),
            new ButtonBind("vivecraft.key.teleportFallback", "left/input/x"),
            new ButtonBind("click", "vivecraft.key.keyboardShift", new HashMap<>(), "button", "left/input/grip", "keyboard"),
            new ButtonBind("click", "vivecraft.key.keyboardShift", new HashMap<>(), "button", "right/input/grip", "keyboard"),
            new ButtonBind("click", "vivecraft.key.keyboardClick", new HashMap<>(), "button", "left/input/trigger", "keyboard"),
            new ButtonBind("click", "vivecraft.key.keyboardClick", new HashMap<>(), "button", "right/input/trigger", "keyboard")
    ));
}