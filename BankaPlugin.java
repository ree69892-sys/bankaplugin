package banka;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.UUID;

public class BankaPlugin extends JavaPlugin {

    private HashMap<UUID, Integer> banka = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("banka").setExecutor(new BankaCommand());
        Bukkit.getScheduler().runTaskTimer(this, this::actionBarGuncelle, 0, 40);
    }

    void actionBarGuncelle() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            int para = banka.getOrDefault(p.getUniqueId(), 0);
            p.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent("§6Banka: §e" + para + " Altın")
            );
        }
    }

    class BankaCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;

            if (args.length != 2 || !args[0].equalsIgnoreCase("yatır")) {
                p.sendMessage("§cKullanım: /banka yatır <miktar>");
                return true;
            }

            int miktar;
            try {
                miktar = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage("§cSayı gir!");
                return true;
            }

            ItemStack altin = new ItemStack(Material.GOLD_INGOT, miktar);

            if (!p.getInventory().containsAtLeast(altin, miktar)) {
                p.sendMessage("§cYeterli altının yok!");
                return true;
            }

            p.getInventory().removeItem(altin);
            banka.put(p.getUniqueId(),
                banka.getOrDefault(p.getUniqueId(), 0) + miktar
            );

            p.sendMessage("§a" + miktar + " altın bankaya yatırıldı!");
            return true;
        }
    }
}
