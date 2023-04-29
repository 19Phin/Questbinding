package net.dialingspoon.questbind.util;

import net.minecraft.client.option.KeyBinding;

import java.io.*;
import java.util.*;

public class KeyBindUtil {
    public static final String[] BUTTONS = {"null","a", "b", "x", "y", "Ltrigger", "Rtrigger", "Lgrip", "Rgrip", "LstickPress", "RstickPress", "MenuButton"};
    public Map<String, Integer> binds = new HashMap<>();
    public boolean read;
    String keys;
    String altKeys;
    String actionsets;
    String save;
    static File oculus_defaults = new File("openvr/input/oculus_defaults.json");
    static File customactionsets = new File("customactionsets.txt");
    static File actualSave = new File("openvr/input/keybinds.txt");

    public void loadKeybinds() {
        try {
            readsave();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        keys = "";
        altKeys = "";
        actionsets = "";
        binds.forEach(this::compileKeybind);
        if (!altKeys.isBlank()) altKeys = altKeys.substring(0, altKeys.length() - 1);
        String defaults = before + altKeys + after + keys + after2;
        replaceFileData(oculus_defaults, defaults);
        replaceFileData(customactionsets, actionsets);
    }

    public void actualSave() {
        System.out.println("AAAAAAA");
        read = false;
        KeyBinding.updateKeysByCode();
        save = "";
        binds.forEach(this::save);
        replaceFileData(actualSave, save);
    }

    private void save(String translationKey, int button){
        save = save + "\n" + translationKey + ":" + button;
    }

    public void readsave() throws IOException {
        Scanner scanner = new Scanner(actualSave);
        if (actualSave.exists() && scanner.hasNext()) {
            Map<String, Integer> map = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(actualSave));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0];
                    int value = Integer.parseInt(parts[1]);
                    map.put(key, value);
                }
            }
            reader.close();
            binds = map;
        } else {
            binds = defaultBinds;
        }
        read = true;
        KeyBinding.updateKeysByCode();
    }

    public void compileKeybind(String translationKey, int button){
        if (!WEIREDONES.contains(translationKey)) {
            keys = keys + mid1 + translationKey + mid2 + BUTTONPATH[button] + mid3 + ",";
        } else if (translationKey.equals(WEIREDONES.get(0))) {
            keys = keys + mid1alt + translationKey + mid2 + BUTTONPATH[button] + mid3 + ",";
        }else {
        altKeys = altKeys + mid1 + translationKey + mid2alt + BUTTONPATH[button] + mid3 + ",";
        }
        actionsets = actionsets + "\n" + translationKey + ":ingame";
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
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String before = """
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
                     "sources" : [
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/contextual/in/vivecraft.key.climbeygrab"
                              }
                           },
                           "parameters": {
                              "haptic_amplitude": "0"
                           },
                           "mode" : "button",
                           "path" : "/user/hand/left/input/trigger"
                        },
                        {
                           "inputs" : {},
                           "mode" : "button",
                           "path" : "/user/hand/left/input/grip"
                        },
                        {
                           "inputs" : {},
                           "mode" : "button",
                           "path" : "/user/hand/right/input/grip"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/contextual/in/vivecraft.key.climbeygrab"
                              }
                           },
                           "parameters": {
                              "haptic_amplitude": "0"
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/trigger"
                        },
                        {
                           "inputs": {
                              "click": {
                                 "output": "/actions/contextual/in/vivecraft.key.climbeygrab"
                              }
                           },
                           "mode": "button",
                           "path": "/user/hand/left/input/grip"
                        },
                        {
                           "inputs": {
                              "click": {
                                 "output": "/actions/contextual/in/vivecraft.key.climbeygrab"
                              }
                           },
                           "mode": "button",
                           "path": "/user/hand/right/input/grip"
                        },
                        {
                           "inputs": {
                              "click": {
                                 "output": "/actions/contextual/in/vivecraft.key.vrinteract"
                              }
                           },
                           "mode": "button",
                           "path": "/user/hand/left/input/grip"
                        },
                        {
                           "inputs": {
                              "click": {
                                 "output": "/actions/contextual/in/vivecraft.key.vrinteract"
                              }
                           },
                           "mode": "button",
                           "path": "/user/hand/right/input/grip"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/contextual/in/vivecraft.key.vrinteract"
                              }
                           },
                           "parameters": {
                              "haptic_amplitude": "0"
                           },
                           "mode" : "button",
                           "path" : "/user/hand/left/input/trigger"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/contextual/in/vivecraft.key.vrinteract"
                              }
                           },
                           "parameters": {
                              "haptic_amplitude": "0"
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/trigger"
                        }
                     ]
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
                     "sources" : [""";
    public static String after = """
                     
                     ]
                  },
                  "/actions/gui" : {
                     "sources" : [
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/gui/in/vivecraft.key.guishift"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/left/input/grip"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/gui/in/vivecraft.key.guimiddleclick"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/grip"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/gui/in/vivecraft.key.guileftclick"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/trigger"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/gui/in/vivecraft.key.guirightclick"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/a"
                        },
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
                     "sources" : [""";
    public static String after2 = """
                     
                     ]
                  },
                  "/actions/keyboard" : {
                     "sources" : [
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/keyboard/in/vivecraft.key.keyboardshift"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/left/input/grip"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/keyboard/in/vivecraft.key.keyboardshift"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/grip"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/keyboard/in/vivecraft.key.keyboardclick"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/left/input/trigger"
                        },
                        {
                           "inputs" : {
                              "click" : {
                                 "output" : "/actions/keyboard/in/vivecraft.key.keyboardclick"
                              }
                           },
                           "mode" : "button",
                           "path" : "/user/hand/right/input/trigger"
                        }
                     ]
                  }
               },
               "category" : "steamvr_input",
               "controller_type" : "oculus_touch",
               "description" : "",
               "name" : "Oculus Touch Defaults",
               "options" : {},
               "simulated_actions" : []
            }""";
    public static String mid1 = """
            
            {
               "inputs" : {
                  "click" : {
                     "output" : "/actions/ingame/in/""";

    public static String mid1alt = """
            
            {
               "inputs" : {
                  "click" : {
                     "output" : "/actions/global/in/""";

    public static String mid2 = """
            "
               }
            },
            "mode" : "button",
            "path" : "/user/hand/""";

    public static String mid2alt = """
            "
               }
            },
            "mode" : "toggle_button",
            "path" : "/user/hand/""";
    public static String mid3 = """
            "
            }""";

    public static final String[] BUTTONPATH = {"null","right/input/a", "right/input/b", "left/input/x", "left/input/y", "left/input/trigger", "right/input/trigger", "left/input/grip", "right/input/grip", "left/input/joystick", "right/input/joystick", "left/input/application_menu"};
    public static final ArrayList<String> WEIREDONES = new ArrayList<>(
            java.util.Arrays.asList("key.sneak", "key.inventory", "vivecraft.key.ingamemenubutton", "vivecraft.key.togglekeyboard"));

    public static Map<String, Integer> defaultBinds = new HashMap<>();

    static {
        defaultBinds.put("key.inventory", 4);
        defaultBinds.put("vivecraft.key.hotbarprev", 7);
        defaultBinds.put("vivecraft.key.hotbarnext", 8);
        defaultBinds.put("key.attack", 6);
        defaultBinds.put("vivecraft.key.teleport", 3);
        defaultBinds.put("vivecraft.key.radialmenu", 2);
        defaultBinds.put("key.use", 5);
        defaultBinds.put("key.jump", 1);
        defaultBinds.put("key.sneak", 10);
        defaultBinds.put("vivecraft.key.teleportfallback", 3);
        defaultBinds.put("vivecraft.key.ingamemenubutton", 11);
    }
}