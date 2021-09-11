package net.tiffit.tconplanner.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.tiffit.tconplanner.screen.PlannerScreen;

import java.util.function.Consumer;

public class SliderWidget extends Widget {

    private final PlannerScreen parent;
    private final Consumer<Integer> listener;
    private final int min, max;
    private double percent;
    private int value;

    public SliderWidget(int x, int y, int width, int height, Consumer<Integer> listener, int min, int max, int value, PlannerScreen parent) {
        super(x, y, width, height, new StringTextComponent(""));
        this.parent = parent;
        this.listener = listener;
        this.min = min;
        this.max = max;
        this.value = value;
        percent = (value - min)/(double)(max-min);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTick) {
        int center = y + height/2;
        PlannerScreen.bindTexture();
        for(int dx = x - 2; dx < x + width + 2; dx++){
            parent.blit(stack, dx, center - 2, 176, 78, 1, 4);
        }
        int sliderX = x + (int)(width*percent);
        parent.blit(stack, sliderX - 2, y, 178, 78, 4, 20);
        FontRenderer font = Minecraft.getInstance().font;
        int minValSize = font.width(min + "");
        drawString(stack, font, min + "", x - minValSize - 5, y + 6, 0xff_ff_ff_ff);
        drawString(stack, font, max + "", x + width + 5, y + 6, 0xff_ff_ff_ff);
        drawCenteredString(stack, font, value + "", sliderX, y + 22, 0xff_ff_ff_ff);
    }

    @Override
    public void onClick(double mx, double my) {
        updateVal(mx);
    }

    @Override
    protected void onDrag(double mx, double my, double dx, double dy) {
        if(mx >= x - 5 && my >= y && mx <= x + width + 5 && my <= y + width) {
            updateVal(mx);
        }
    }

    private void updateVal(double mouseX){
        percent = MathHelper.clamp((mouseX - x)/width, 0, 1);
        int oldVal = value;
        value = (int)MathHelper.clamp((max-min)*percent + min, min, max);
        if(value != oldVal)listener.accept(value);
    }
}
