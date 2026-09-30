# BotaniaTubes

## Overview

Tired of struggling to automate Botania's **Petal Apothecary**? Frustrated that fluid pipes don't work with it? You're not alone! This mod provides a simple solution by allowing the Petal Apothecary to interact with fluid input and output from any mod, making automation easier than ever.

## Features

- **Fluid Input & Output:** Connect any fluid transport system to the Petal Apothecary.  
- **Automation Friendly:** Seamlessly integrates with various automation setups.  
- **Mod Compatibility:** Works with any mod that provides fluid pipes or fluid handling.  
- **Fractional Filling:** Pipes can insert and extract any amount (e.g. 250 mB per tick), not just full buckets.  
- **Visible Fill Level:** The liquid in the apothecary rises and falls with the amount inside, with a smooth animation.  
- **Fractional Containers:** Right-clicking with tanks that handle partial amounts (e.g. Mekanism's Fluid Tank) fills or empties the fraction.  
- **Lightweight & Efficient:** No unnecessary overhead; just plug and play!  

## Installation

1. Download the latest version of the mod from the [BotaniaTubes](https://github.com/MaxiusEden/BotaniaTubes) page.  
2. Ensure you have **Minecraft Forge** installed for Minecraft 1.20.1.  
3. Place the downloaded `.jar` file into your `mods` folder, on both the **client** and the **server**.  
4. Launch Minecraft and enjoy automated apothecary filling!  

## Requirements

- **Minecraft Version:** `1.20.1`  
- **Mod Loader:** Forge `47.1.3` or newer  
- **Dependencies:** [Botania by Vazkii](https://www.curseforge.com/minecraft/mc-mods/botania) `1.20.1-446` or newer  
- **Sides:** required on both client and server. Pipes only show their connection to the apothecary when the client has the mod, and the client draws the fill level.  

## Acknowledgment

This mod is an **addon for Botania**, a mod created by *Vazkii*. All rights and ownership of Botania belong to them. You can check out the original mod here: [Botania on CurseForge](https://www.curseforge.com/minecraft/mc-mods/botania)  

## How to Use

1. **Place the Petal Apothecary** in your world.  
2. **Connect fluid pipes** from any mod that supports fluid transport (e.g., Mekanism, Thermal Expansion, etc.).  
3. **Automate water supply** to continuously keep the apothecary filled.  
4. **Enjoy hands-free crafting** of Botania recipes without manual refilling.  

## Behavior

- Only water and lava are accepted, the same fluids Botania supports.  
- Botania only has "full" and "empty" apothecaries, and recipes only work when it is full. A partial amount is kept by this mod until it reaches a full bucket.  
- A bucket (or rain, or a thrown bucket) of the **same** fluid fills the apothecary and replaces the partial amount.  
- A different fluid is refused while a partial amount is inside. Pipe it out or break the apothecary to switch fluids.  
- Breaking the apothecary discards the partial amount.  

## Known Issues

- Other mods that change the apothecary's fluid through Botania's API are not blocked from replacing a partial amount of a different fluid.  

Please report any issues on the [Issues](https://github.com/MaxiusEden/BotaniaTubes/issues) page.  

## Modpacks

Feel free to include this mod in any modpack. Credit and a link to this repository are appreciated.  

## License

This project is licensed under the MIT License. See the [`LICENSE`](LICENSE) file for details.  

## Support

If you have any questions, suggestions, or bug reports, feel free to open an issue or reach out via Discord.  

---

Enjoy automating your Petal Apothecary and expanding your Botania setups with ease!
