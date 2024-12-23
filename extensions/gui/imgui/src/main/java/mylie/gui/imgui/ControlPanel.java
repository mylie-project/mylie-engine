package mylie.gui.imgui;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.extension.implot.ImPlot;
import imgui.extension.implot.flag.ImPlotAxis;
import imgui.extension.implot.flag.ImPlotAxisFlags;
import imgui.extension.implot.flag.ImPlotFlags;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import java.util.function.Supplier;
import mylie.core.Timer;

public class ControlPanel implements ImGuiComponent {
	private final int windowLocation;
	final float pad = 10;
	private final DataGraph fpsGraph;
	private Timer.Time time;
	public ControlPanel(int windowLocation) {
		this.windowLocation = windowLocation;
		fpsGraph = new DataGraph("FPS", 25, 0.1f, this::getFrameTime);
	}

	@Override
	public void renderImGui(Timer.Time time) {
		this.time = time;
		ImGuiViewport viewport = ImGui.getMainViewport();
		ImVec2 workPos = viewport.getWorkPos();
		ImVec2 workSize = viewport.getWorkSize();
		ImVec2 windowPosition = new ImVec2();
		ImVec2 windowPivot = new ImVec2();
		windowPosition.x = (windowLocation & 1) != 0 ? workPos.x + workSize.x - pad : workPos.x + pad;
		windowPosition.y = (windowLocation & 2) != 0 ? workPos.y + workSize.y - pad : workPos.y + pad;
		windowPivot.x = (windowLocation & 1) != 0 ? 1 : 0;
		windowPivot.y = (windowLocation & 2) != 0 ? 1 : 0;
		ImGui.setNextWindowPos(windowPosition, ImGuiCond.Always, windowPivot);
		ImGui.setNextWindowSize(new ImVec2(400, 0));
		ImGui.begin("MyLiE Engine",
				ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize);
		ImGui.text("MyLiE - Control Panel");
		fpsGraph.update(time);
		if (ImGui.collapsingHeader("FrameTime Graph")) {
			fpsGraph.render(time);
		}
		ImGui.separator();

		ImGui.end();
	}

	private float getFrameTime() {
		return (float) time.delta();
	}

	static class DataGraph {
		String title;
		float updateInterval;
		float[] data;
		float currData;
		int currDataCount;
		float currTime;
		Supplier<Float> dataSupplier;

		public DataGraph(String title, int historyLength, float updateInterval, Supplier<Float> dataSupplier) {
			this.title = title;
			this.updateInterval = updateInterval;
			data = new float[historyLength];
			this.dataSupplier = dataSupplier;
			currData = 0;
			currDataCount = 0;
			currTime = 0;
		}

		void update(Timer.Time time) {
			currTime += (float) time.delta();
			if (currTime > updateInterval) {
				currTime = 0;
				for (int i = 0; i < data.length - 1; i++) {
					data[i] = data[i + 1];
				}
				data[data.length - 1] = currData / currDataCount;
				currData = 0;
				currDataCount = 0;
			}
			currDataCount++;
			currData += dataSupplier.get();
		}
		void render(Timer.Time time) {
			ImPlot.beginPlot(title, new ImVec2(390, 100),
					ImPlotFlags.NoMenus | ImPlotFlags.NoInputs | ImPlotFlags.NoTitle | ImPlotFlags.NoLegend);
			ImPlot.setupAxis(ImPlotAxis.Y1, "ms", ImPlotAxisFlags.AutoFit | ImPlotAxisFlags.NoMenus
					| ImPlotAxisFlags.NoLabel | ImPlotAxisFlags.NoGridLines);
			ImPlot.setupAxis(ImPlotAxis.X1, "ms", ImPlotAxisFlags.AutoFit | ImPlotAxisFlags.NoMenus
					| ImPlotAxisFlags.NoLabel | ImPlotAxisFlags.NoDecorations);
			ImPlot.plotLine(title, data, data.length);
			ImPlot.endPlot();
		}
	}
}
