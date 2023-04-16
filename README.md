Questbind is an experimental mod for Minecraft that allows players to customize the keybindings for Quescraft.

It works by redirecting the "oculus_defaults.json" folder used by Vivecraft to set the keybindings, to instead read it in the Minecraft config folder.

Questbind is currently in very early beta and has many issues as vivecraft really doesn't expect this.

You will need basic knowledge of how to read JSON files to use.

Copy the "oculus_defaults.json" file located at ".minecraft/openvr/input" to the ".minecraft/config" folder. From there, you can edit the bindings contained in the JSON file. The format for most binds is:
{
    "inputs" : {

"click" : {

    "output" : "<the translation key for the key output>"

}

    },
    "mode" : "button",
    "path" : "<the path of the controller button, which can probably be found elsewhere in the file>"

},
