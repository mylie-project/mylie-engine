package mylie.lwjgl3.glfw;

import static org.lwjgl.glfw.GLFW.*;

import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;

public class GlfwConvert {
	public static Mouse.Button convertMouseButton(int button) {
		return switch (button) {
			case GLFW_MOUSE_BUTTON_LEFT -> Mouse.Button.LEFT;
			case GLFW_MOUSE_BUTTON_RIGHT -> Mouse.Button.RIGHT;
			case GLFW_MOUSE_BUTTON_MIDDLE -> Mouse.Button.MIDDLE;
			case GLFW_MOUSE_BUTTON_4 -> Mouse.Button.BUTTON_4;
			case GLFW_MOUSE_BUTTON_5 -> Mouse.Button.BUTTON_5;
			case GLFW_MOUSE_BUTTON_6 -> Mouse.Button.BUTTON_6;
			case GLFW_MOUSE_BUTTON_7 -> Mouse.Button.BUTTON_7;
			case GLFW_MOUSE_BUTTON_8 -> Mouse.Button.BUTTON_8;
			default -> Mouse.Button.UNKNOWN;
		};
	}

	public static Keyboard.Key convertKey(int scancode, int keycode) {
		Keyboard.Key engineKey = switch (keycode) {
			case GLFW_KEY_SPACE -> Keyboard.Key.SPACE;
			case GLFW_KEY_APOSTROPHE -> Keyboard.Key.APOSTROPHE;
			case GLFW_KEY_COMMA -> Keyboard.Key.COMMA;
			case GLFW_KEY_MINUS -> Keyboard.Key.MINUS;
			case GLFW_KEY_PERIOD -> Keyboard.Key.PERIOD;
			case GLFW_KEY_SLASH -> Keyboard.Key.SLASH;
			case GLFW_KEY_0 -> Keyboard.Key.NUM_0;
			case GLFW_KEY_1 -> Keyboard.Key.NUM_1;
			case GLFW_KEY_2 -> Keyboard.Key.NUM_2;
			case GLFW_KEY_3 -> Keyboard.Key.NUM_3;
			case GLFW_KEY_4 -> Keyboard.Key.NUM_4;
			case GLFW_KEY_5 -> Keyboard.Key.NUM_5;
			case GLFW_KEY_6 -> Keyboard.Key.NUM_6;
			case GLFW_KEY_7 -> Keyboard.Key.NUM_7;
			case GLFW_KEY_8 -> Keyboard.Key.NUM_8;
			case GLFW_KEY_9 -> Keyboard.Key.NUM_9;
			case GLFW_KEY_SEMICOLON -> Keyboard.Key.SEMICOLON;
			case GLFW_KEY_EQUAL -> Keyboard.Key.EQUAL;
			case GLFW_KEY_A -> Keyboard.Key.A;
			case GLFW_KEY_B -> Keyboard.Key.B;
			case GLFW_KEY_C -> Keyboard.Key.C;
			case GLFW_KEY_D -> Keyboard.Key.D;
			case GLFW_KEY_E -> Keyboard.Key.E;
			case GLFW_KEY_F -> Keyboard.Key.F;
			case GLFW_KEY_G -> Keyboard.Key.G;
			case GLFW_KEY_H -> Keyboard.Key.H;
			case GLFW_KEY_I -> Keyboard.Key.I;
			case GLFW_KEY_J -> Keyboard.Key.J;
			case GLFW_KEY_K -> Keyboard.Key.K;
			case GLFW_KEY_L -> Keyboard.Key.L;
			case GLFW_KEY_M -> Keyboard.Key.M;
			case GLFW_KEY_N -> Keyboard.Key.N;
			case GLFW_KEY_O -> Keyboard.Key.O;
			case GLFW_KEY_P -> Keyboard.Key.P;
			case GLFW_KEY_Q -> Keyboard.Key.Q;
			case GLFW_KEY_R -> Keyboard.Key.R;
			case GLFW_KEY_S -> Keyboard.Key.S;
			case GLFW_KEY_T -> Keyboard.Key.T;
			case GLFW_KEY_U -> Keyboard.Key.U;
			case GLFW_KEY_V -> Keyboard.Key.V;
			case GLFW_KEY_W -> Keyboard.Key.W;
			case GLFW_KEY_X -> Keyboard.Key.X;
			case GLFW_KEY_Y -> Keyboard.Key.Y;
			case GLFW_KEY_Z -> Keyboard.Key.Z;
			case GLFW_KEY_LEFT_BRACKET -> Keyboard.Key.LEFT_BRACKET;
			case GLFW_KEY_BACKSLASH -> Keyboard.Key.BACKSLASH;
			case GLFW_KEY_RIGHT_BRACKET -> Keyboard.Key.RIGHT_BRACKET;
			case GLFW_KEY_GRAVE_ACCENT -> Keyboard.Key.GRAVE_ACCENT;
			case GLFW_KEY_WORLD_1 -> Keyboard.Key.WORLD_1;
			case GLFW_KEY_WORLD_2 -> Keyboard.Key.WORLD_2;
			case GLFW_KEY_ESCAPE -> Keyboard.Key.ESCAPE;
			case GLFW_KEY_ENTER -> Keyboard.Key.ENTER;
			case GLFW_KEY_TAB -> Keyboard.Key.TAB;
			case GLFW_KEY_BACKSPACE -> Keyboard.Key.BACKSPACE;
			case GLFW_KEY_INSERT -> Keyboard.Key.INSERT;
			case GLFW_KEY_DELETE -> Keyboard.Key.DELETE;
			case GLFW_KEY_RIGHT -> Keyboard.Key.RIGHT;
			case GLFW_KEY_LEFT -> Keyboard.Key.LEFT;
			case GLFW_KEY_DOWN -> Keyboard.Key.DOWN;
			case GLFW_KEY_UP -> Keyboard.Key.UP;
			case GLFW_KEY_PAGE_UP -> Keyboard.Key.PAGE_UP;
			case GLFW_KEY_PAGE_DOWN -> Keyboard.Key.PAGE_DOWN;
			case GLFW_KEY_HOME -> Keyboard.Key.HOME;
			case GLFW_KEY_END -> Keyboard.Key.END;
			case GLFW_KEY_CAPS_LOCK -> Keyboard.Key.CAPS_LOCK;
			case GLFW_KEY_SCROLL_LOCK -> Keyboard.Key.SCROLL_LOCK;
			case GLFW_KEY_NUM_LOCK -> Keyboard.Key.NUM_LOCK;
			case GLFW_KEY_PRINT_SCREEN -> Keyboard.Key.PRINT_SCREEN;
			case GLFW_KEY_PAUSE -> Keyboard.Key.PAUSE;
			case GLFW_KEY_F1 -> Keyboard.Key.F1;
			case GLFW_KEY_F2 -> Keyboard.Key.F2;
			case GLFW_KEY_F3 -> Keyboard.Key.F3;
			case GLFW_KEY_F4 -> Keyboard.Key.F4;
			case GLFW_KEY_F5 -> Keyboard.Key.F5;
			case GLFW_KEY_F6 -> Keyboard.Key.F6;
			case GLFW_KEY_F7 -> Keyboard.Key.F7;
			case GLFW_KEY_F8 -> Keyboard.Key.F8;
			case GLFW_KEY_F9 -> Keyboard.Key.F9;
			case GLFW_KEY_F10 -> Keyboard.Key.F10;
			case GLFW_KEY_F11 -> Keyboard.Key.F11;
			case GLFW_KEY_F12 -> Keyboard.Key.F12;
			case 320 -> Keyboard.Key.NUMPAD_0;
			case 321 -> Keyboard.Key.NUMPAD_1;
			case 322 -> Keyboard.Key.NUMPAD_2;
			case 323 -> Keyboard.Key.NUMPAD_3;
			case 324 -> Keyboard.Key.NUMPAD_4;
			case 325 -> Keyboard.Key.NUMPAD_5;
			case 326 -> Keyboard.Key.NUMPAD_6;
			case 327 -> Keyboard.Key.NUMPAD_7;
			case 328 -> Keyboard.Key.NUMPAD_8;
			case 329 -> Keyboard.Key.NUMPAD_9;
			case 335 -> Keyboard.Key.NUMPAD_ENTER;
			case 334 -> Keyboard.Key.NUMPAD_PLUS;
			case 333 -> Keyboard.Key.NUMPAD_MINUS;
			case 332 -> Keyboard.Key.NUMPAD_TIMES;
			case 331 -> Keyboard.Key.NUMPAD_DIVIDE;
			case 330 -> Keyboard.Key.NUMPAD_DELETE;
			default -> Keyboard.Key.UNKNOWN;
		};
		if (engineKey == Keyboard.Key.UNKNOWN) {
			engineKey = switch (scancode) {
				case 0x1D -> Keyboard.Key.LEFT_CONTROL;
				case 0x2A -> Keyboard.Key.LEFT_SHIFT;
				case 0x38 -> Keyboard.Key.LEFT_ALT;
				case 347 -> Keyboard.Key.LEFT_SUPER;
				case 0x36 -> Keyboard.Key.RIGHT_SHIFT;
				case 0x1C -> Keyboard.Key.ENTER;
				case 285 -> Keyboard.Key.RIGHT_CONTROL;
				case 312 -> Keyboard.Key.RIGHT_ALT;
				case 349 -> Keyboard.Key.RIGHT_SUPER;
				case 0x3A -> Keyboard.Key.CAPS_LOCK;
				default -> Keyboard.Key.UNKNOWN;
			};
		}
		return engineKey;
	}
}
