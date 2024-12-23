package mylie.gui.imgui;

import imgui.flag.ImGuiKey;
import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;

public class ImGuiUtil {

	static int convertMouseButton(Mouse.Button button) {
		if (button == Mouse.Button.LEFT) {
			return 0;
		}
		if (button == Mouse.Button.RIGHT) {
			return 1;
		}
		if (button == Mouse.Button.MIDDLE) {
			return 2;
		}
		return -1;
	}

	public static int toImgui(Keyboard.Key key) {
		return switch (key.name()) {
			case "SPACE" -> ImGuiKey.Space;
			case "ENTER" -> ImGuiKey.Enter;
			case "ESCAPE" -> ImGuiKey.Escape;
			case "UP" -> ImGuiKey.UpArrow;
			case "DOWN" -> ImGuiKey.DownArrow;
			case "LEFT" -> ImGuiKey.LeftArrow;
			case "RIGHT" -> ImGuiKey.RightArrow;
			case "W" -> ImGuiKey.W;
			case "A" -> ImGuiKey.A;
			case "B" -> ImGuiKey.B;
			case "C" -> ImGuiKey.C;
			case "D" -> ImGuiKey.D;
			case "E" -> ImGuiKey.E;
			case "F" -> ImGuiKey.F;
			case "G" -> ImGuiKey.G;
			case "H" -> ImGuiKey.H;
			case "I" -> ImGuiKey.I;
			case "J" -> ImGuiKey.J;
			case "K" -> ImGuiKey.K;
			case "L" -> ImGuiKey.L;
			case "M" -> ImGuiKey.M;
			case "N" -> ImGuiKey.N;
			case "O" -> ImGuiKey.O;
			case "P" -> ImGuiKey.P;
			case "Q" -> ImGuiKey.Q;
			case "R" -> ImGuiKey.R;
			case "S" -> ImGuiKey.S;
			case "T" -> ImGuiKey.T;
			case "U" -> ImGuiKey.U;
			case "V" -> ImGuiKey.V;
			case "X" -> ImGuiKey.X;
			case "Y" -> ImGuiKey.Y;
			case "Z" -> ImGuiKey.Z;
			case "NUM_0" -> ImGuiKey._0;
			case "NUM_1" -> ImGuiKey._1;
			case "NUM_2" -> ImGuiKey._2;
			case "NUM_3" -> ImGuiKey._3;
			case "NUM_4" -> ImGuiKey._4;
			case "NUM_5" -> ImGuiKey._5;
			case "NUM_6" -> ImGuiKey._6;
			case "NUM_7" -> ImGuiKey._7;
			case "NUM_8" -> ImGuiKey._8;
			case "NUM_9" -> ImGuiKey._9;
			case "NUMPAD_0" -> ImGuiKey.Keypad0;
			case "NUMPAD_1" -> ImGuiKey.Keypad1;
			case "NUMPAD_2" -> ImGuiKey.Keypad2;
			case "NUMPAD_3" -> ImGuiKey.Keypad3;
			case "NUMPAD_4" -> ImGuiKey.Keypad4;
			case "NUMPAD_5" -> ImGuiKey.Keypad5;
			case "NUMPAD_6" -> ImGuiKey.Keypad6;
			case "NUMPAD_7" -> ImGuiKey.Keypad7;
			case "NUMPAD_8" -> ImGuiKey.Keypad8;
			case "NUMPAD_9" -> ImGuiKey.Keypad9;
			case "NUMPAD_ENTER" -> ImGuiKey.KeypadEnter;
			case "NUMPAD_PLUS" -> ImGuiKey.KeypadAdd;
			case "NUMPAD_MINUS" -> ImGuiKey.KeypadSubtract;
			case "NUMPAD_TIMES" -> ImGuiKey.KeypadMultiply;
			case "NUMPAD_DIVIDE" -> ImGuiKey.KeypadDivide;
			case "NUMPAD_DELETE" -> ImGuiKey.KeypadDecimal;
			case "SEMICOLON" -> ImGuiKey.Semicolon;
			case "EQUAL" -> ImGuiKey.Equal;
			case "LEFT_BRACKET" -> ImGuiKey.LeftBracket;
			case "BACKSLASH" -> ImGuiKey.Backslash;
			case "RIGHT_BRACKET" -> ImGuiKey.RightBracket;
			case "GRAVE_ACCENT" -> ImGuiKey.GraveAccent;
			case "TAB" -> ImGuiKey.Tab;
			case "BACKSPACE" -> ImGuiKey.Backspace;
			case "INSERT" -> ImGuiKey.Insert;
			case "DELETE" -> ImGuiKey.Delete;
			case "PAGE_UP" -> ImGuiKey.PageUp;
			case "PAGE_DOWN" -> ImGuiKey.PageDown;
			case "HOME" -> ImGuiKey.Home;
			case "END" -> ImGuiKey.End;
			case "CAPS_LOCK" -> ImGuiKey.CapsLock;
			case "SCROLL_LOCK" -> ImGuiKey.ScrollLock;
			case "NUM_LOCK" -> ImGuiKey.NumLock;
			case "PRINT_SCREEN" -> ImGuiKey.PrintScreen;
			case "PAUSE" -> ImGuiKey.Pause;
			case "F1" -> ImGuiKey.F1;
			case "F2" -> ImGuiKey.F2;
			case "F3" -> ImGuiKey.F3;
			case "F4" -> ImGuiKey.F4;
			case "F5" -> ImGuiKey.F5;
			case "F6" -> ImGuiKey.F6;
			case "F7" -> ImGuiKey.F7;
			case "F8" -> ImGuiKey.F8;
			case "F9" -> ImGuiKey.F9;
			case "F10" -> ImGuiKey.F10;
			case "F11" -> ImGuiKey.F11;
			case "F12" -> ImGuiKey.F12;
			case "APOSTROPHE" -> ImGuiKey.Apostrophe;
			case "COMMA" -> ImGuiKey.Comma;
			case "MINUS" -> ImGuiKey.Minus;
			case "PERIOD" -> ImGuiKey.Period;
			case "SLASH" -> ImGuiKey.Slash;
			case "LEFT_SHIFT" -> ImGuiKey.LeftShift;
			case "LEFT_ALT" -> ImGuiKey.LeftAlt;
			case "LEFT_SUPER" -> ImGuiKey.LeftSuper;
			case "RIGHT_SHIFT" -> ImGuiKey.RightShift;
			case "RIGHT_ALT" -> ImGuiKey.RightAlt;
			case "RIGHT_SUPER" -> ImGuiKey.RightSuper;
			case "UNKNOWN" -> ImGuiKey.None;
			default -> ImGuiKey.None;
		};
	}
}
