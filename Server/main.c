#include <allegro5/allegro.h>
#include <allegro5/allegro_font.h>
#include <allegro5/allegro_image.h>
#include <allegro5/allegro_native_dialog.h>
#include <allegro5/allegro_primitives.h>
#include <allegro5/allegro_ttf.h>

#include <stdlib.h>
#include <string.h>

#include "constants.h"

// Game Screen
ALLEGRO_DISPLAY *display = NULL;
// Event Queue detected
ALLEGRO_EVENT_QUEUE *event_queue = NULL;

// Timers
ALLEGRO_TIMER *timer = NULL;

// Images
ALLEGRO_BITMAP *img_logo = NULL;
ALLEGRO_BITMAP *img_blue_player = NULL;
ALLEGRO_BITMAP *img_purple_player = NULL;
ALLEGRO_BITMAP *img_red_player = NULL;
ALLEGRO_BITMAP *img_white_player = NULL;

// Colors
ALLEGRO_COLOR BLACK;
ALLEGRO_COLOR BLUE;
ALLEGRO_COLOR GREEN;
ALLEGRO_COLOR PURPLE;
ALLEGRO_COLOR RED;
ALLEGRO_COLOR YELLOW;
ALLEGRO_COLOR WHITE;

// Fonts
ALLEGRO_FONT *font = NULL;
ALLEGRO_FONT *font_game = NULL;
ALLEGRO_FONT *font_players = NULL;

bool play;
bool wait;

int color = 0;
int wait_time;
int waiting = 0;

//********************************TEST********************************
int num_players = 4;
char *nicknames[4] = {"Allan", "Erick", "Christopher", "Marco"};
char *colors[4] = {"blue", "purple", "red", "white"};
//********************************TEST********************************

// This function start all components
bool init_all() {
	// Init allegro library
	if (!al_init()) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to initialize allegro",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
		return false;
	}

	// Create timer
	timer = al_create_timer(1.0 / FPS_HIGHLIGHT);
	if (!timer) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to create timer",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
		return false;
	}
	// Start timer
	al_start_timer(timer);

	// Create the display
	display = al_create_display(WINDOW_WIDTH, WINDOW_HEIGHT);
	if (!display) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to create display",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
		return false;
	}
	// Set window title
	al_set_window_title(display, WINDOW_TITLE);

	// Init allegro images
	if(!al_init_image_addon()) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to initialize image_addon",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
      return false;
    }

    // Create event queue
	event_queue = al_create_event_queue();
	if (!event_queue) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to create event queue",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
		return false;
	}
	
	// Init Allegro Fonts
	if (!al_init_font_addon()) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to init font",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
    	return false;
    }

    // Init allegro true type font
	if (!al_init_ttf_addon()) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to init ttf",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
    	return false;
	}

	// Load all fonts
	font = al_load_ttf_font(FONT_PATH, FONT_SIZE, 0);
	font_game = al_load_ttf_font(FONT_PATH, FONT_SIZE_GAME, 0);
    font_players = al_load_ttf_font(FONT_PATH, FONT_SIZE_PLAYERS, 0);
	if ((!font) && (!font_game) && (!font_players)) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Could not load font",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
      return false;
    }

    // Init primitives figures
	if (!al_init_primitives_addon()) {
		al_show_native_message_box(NULL, NULL, "Error",
								"Failed to init primitives",
								NULL, ALLEGRO_MESSAGEBOX_ERROR);
      return false;
    }

    // Init keyboard detected
    al_install_keyboard();

	// Register all events of display and timer
	al_register_event_source(event_queue,
							al_get_display_event_source(display));
	al_register_event_source(event_queue,
							al_get_timer_event_source(timer));
    al_register_event_source(event_queue,
   							al_get_keyboard_event_source());

    // Init all colors
    BLACK = al_map_rgb(0, 0, 0);
    BLUE = al_map_rgb(0, 0, 225);
    GREEN = al_map_rgb(0, 255, 0);
    PURPLE =al_map_rgb(100, 0, 255);
    RED =  al_map_rgb(255, 0, 0);
    YELLOW = al_map_rgb(255, 255, 0);
    WHITE = al_map_rgb(255, 255, 255);

	return true;
}

// Delete all started components
void destroy_all() {
	al_destroy_display(display);
	al_destroy_event_queue(event_queue);

	al_destroy_timer(timer);

	al_destroy_font(font);
	al_destroy_font(font_players);

	al_uninstall_keyboard();
	
	al_destroy_bitmap(img_logo);
	al_destroy_bitmap(img_blue_player);
	al_destroy_bitmap(img_purple_player);
	al_destroy_bitmap(img_red_player);
	al_destroy_bitmap(img_white_player);
}

// Load all images
void load_imgs() {
	img_logo = al_load_bitmap(IMG_LOGO_PATH);
	img_blue_player = al_load_bitmap(IMG_BLUE_PLAYER_PATH);
	img_purple_player = al_load_bitmap(IMG_PURPLE_PLAYER_PATH);       
	img_red_player = al_load_bitmap(IMG_RED_PLAYER_PATH);
	img_white_player = al_load_bitmap(IMG_WHITE_PLAYER_PATH);
}

// Update the menu graphics
void update_menu()
{
	// Clear window
	al_clear_to_color(BLACK);
	// Define text_color
	ALLEGRO_COLOR text_color;

	// Indicate the text color Yellow o Red
	if (color) text_color = YELLOW;
	else text_color = RED;

	// Draw all texts
	al_draw_text(font, text_color, WINDOW_WIDTH/2,
				WINDOW_HEIGHT/2 + 50, ALLEGRO_ALIGN_CENTRE,
				"Press Enter to");
	al_draw_text(font, text_color, WINDOW_WIDTH/2,
				WINDOW_HEIGHT/2 + 100, ALLEGRO_ALIGN_CENTRE,
				"start server");
	
	// Change the text color from Yellow to Red
	color = 1 - color;
	// Draw logo
	al_draw_bitmap(img_logo, 0, 0, 0);
}

// Update the graphics of the players window
void update_players_window() {
	// Clear window
	al_clear_to_color(al_map_rgb(0, 0, 0));
	
	// Define text_color
	ALLEGRO_COLOR text_color;
	
	// If time is less than 10s, set red to text
	if ((int)(wait_time/2.5) <= 10) text_color = RED;
	// If time is less than 20s, set yellow to text
	else if ((int)(wait_time/2.5) <= 20) text_color = YELLOW;
	// If time is more than 20s, set green to text
	else text_color = GREEN;

	// Draw text
	al_draw_text(font, WHITE, WINDOW_WIDTH/2, 10,
				ALLEGRO_ALIGN_CENTRE, WAITING_PLAYERS_TEXT[waiting]);
	al_draw_arc(WINDOW_WIDTH - 50.0, 50.0, 15.0, -PI/2,
				(((int)(wait_time/2.5))/60.0)*2.0*PI, text_color, 30);
	al_draw_textf(font, text_color, WINDOW_WIDTH - 120, 20,
				ALLEGRO_ALIGN_CENTRE, "%d", (int)(wait_time/2.5));

	ALLEGRO_BITMAP *img = NULL;

	int i = 0;
	for (; i < num_players; ++i) {

		ALLEGRO_COLOR text_color;

		if (strcmp(colors[i], "blue") == 0) {
			text_color = BLUE;
			img = img_blue_player;
		}
		else if (strcmp(colors[i], "purple") == 0) {
			text_color = PURPLE;
			img = img_purple_player;
		}
		else if (strcmp(colors[i], "red") == 0) {
			text_color = RED;
			img = img_red_player;
		}
		else {
			text_color = WHITE;
			img = img_white_player;
		}

		al_draw_bitmap(img, POSX_IMGS_WAIT, POSY_WAIT[i][0], 0);
		al_draw_textf(font_players, text_color, POSX_TEXTS_WAIT,
					POSY_WAIT[i][0], ALLEGRO_ALIGN_CENTRE,
					"Player %d", i + 1);
		al_draw_text(font_players, text_color, POSX_TEXTS_WAIT,
					POSY_WAIT[i][1], ALLEGRO_ALIGN_CENTRE,
					nicknames[i]);
	}
	img = NULL;
	al_destroy_bitmap(img);

	// Wait time to start game 
	wait_time--;
	// Time to change the text loading
	waiting++;
	
	if (waiting == 4) waiting = 0;
}

void update_game_window() {
	// Clear window
	al_clear_to_color(al_map_rgb(0, 0, 0));

	// Draw all text
	al_draw_text(font, WHITE, WINDOW_WIDTH/2, 10,
				ALLEGRO_ALIGN_CENTRE, "PLAY");

	ALLEGRO_BITMAP *img = NULL;

	int i = 0;
	for (; i < num_players; ++i) {
		// Draw division lines
		al_draw_line(WINDOW_WIDTH*i/num_players, WINDOW_HEIGHT - 100,
				WINDOW_WIDTH*i/num_players, WINDOW_HEIGHT, WHITE, 2);

		ALLEGRO_COLOR text_color;

		if (strcmp(colors[i], "blue") == 0) {
			text_color = BLUE;
			img = img_blue_player;
		}
		else if (strcmp(colors[i], "purple") == 0) {
			text_color = PURPLE;
			img = img_purple_player;
		}
		else if (strcmp(colors[i], "red") == 0) {
			text_color = RED;
			img = img_red_player;
		}
		else {
			text_color = WHITE;
			img = img_white_player;
		}
		
		al_draw_bitmap(img, POSX_GAME[i][0], POSY_IMGS_GAME, 0);
		al_draw_textf(font_game, text_color, POSX_GAME[i][1],
					POSY_TEXT_PLAYER_GAME, ALLEGRO_ALIGN_CENTRE,
					"Player %d", i + 1);
		al_draw_text(font_game, text_color, POSX_GAME[i][1],
					POSY_TEXT_NICKNAME_GAME, ALLEGRO_ALIGN_CENTRE,
					nicknames[i]);
		al_draw_textf(font_game, text_color, POSX_GAME[i][1],
					POSY_TEXT_SCORE_GAME, ALLEGRO_ALIGN_CENTRE,
					"Score %d", 0);
		al_draw_textf(font_game, text_color, POSX_GAME[i][1],
					POSY_TEXT_LIFES_GAME, ALLEGRO_ALIGN_CENTRE,
					"Lifes %d", 2);
	}

	img = NULL;
	al_destroy_bitmap(img);

	al_draw_line(WINDOW_WIDTH*i/num_players, WINDOW_HEIGHT - 100,
				WINDOW_WIDTH, WINDOW_HEIGHT, WHITE, 2);
	al_draw_line(0, WINDOW_HEIGHT - 100, WINDOW_WIDTH,
				WINDOW_HEIGHT - 100, WHITE, 2);
	al_draw_line(0, WINDOW_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT,
				WHITE, 2);
}

// Show the mainwindow
void show_mainwindow() {
	// True to show menu
	bool menu = true;
	// Indicates when graphics are drawn
	bool redraw = false;
	
	while (menu) {
		// Get event from queue
		ALLEGRO_EVENT event;
      	al_wait_for_event(event_queue, &event);

		switch (event.type) {
			// When app is close, exit
			case ALLEGRO_EVENT_DISPLAY_CLOSE:
				menu = false;
				play = false;
				wait = false;
				break;
			// When timer launch a event
			case ALLEGRO_EVENT_TIMER:
				redraw = true;
				break;
			// When key was pressed
			case ALLEGRO_EVENT_KEY_DOWN:

				switch(event.keyboard.keycode) {
					// When key escape was pressed, exit
		            case ALLEGRO_KEY_ESCAPE:
		                menu = false;
		                play = false;
		                wait = false;
		                break;
		            // When enter was pressed, go to waiting window
		            case ALLEGRO_KEY_ENTER:
		                menu = false;
		                wait = true;
		            	break;
		            default:
		            	break;
		        }
				break;
			default:
				break;
		}
		// Update menu
		if (redraw) {
			update_menu();
			redraw = false;
		}
		// Update display
        al_flip_display();
	}
}

// Show the players window
void show_players_window() {
	// Indicates when graphics are drawn
	bool redraw = false;

	// Run until the time runs out, four players are connected or
	// close the window
	while ((wait_time >= 0) && wait) {
		// Get event from queue
		ALLEGRO_EVENT event;
      	al_wait_for_event(event_queue, &event);

		switch (event.type) {
			// When app is close, exit
			case ALLEGRO_EVENT_DISPLAY_CLOSE:
				play = false;
				wait = false;
				break;
			// When timer launch a event
			case ALLEGRO_EVENT_TIMER:
				redraw = true;
				break;
			// When key was pressed
			case ALLEGRO_EVENT_KEY_DOWN:

				switch(event.keyboard.keycode) {
					// When key escape was pressed, exit
		            case ALLEGRO_KEY_ESCAPE:
		               play = false;
		               wait = false;
		               break;
		        }
				break;

			default:
				break;
		}
		// Update player window
		if (redraw) {
			update_players_window();
			redraw = false;
		}
		// Update display
        al_flip_display();
	}
	// When time runs out, start game
	if (wait_time <= 0)	play = true;
}

// Show window where game is showed
void show_game_window() {
	// Indicates when graphics are drawn
	bool redraw = false;
	
	while (play) {
		// Get event from queue
		ALLEGRO_EVENT event;
      	al_wait_for_event(event_queue, &event);

		switch (event.type) {
			// When app is close, exit
			case ALLEGRO_EVENT_DISPLAY_CLOSE:
				play = false;
				break;
			// When timer launch a event
			case ALLEGRO_EVENT_TIMER:
				redraw = true;
				break;
			// When key was pressed
			case ALLEGRO_EVENT_KEY_DOWN:

				switch(event.keyboard.keycode) {
					// When key escape was pressed, exit
		            case ALLEGRO_KEY_ESCAPE:
		               play = false;
		               break;
		        }
				break;

			default:
				break;
		}
		// Update game window
		if (redraw) {
			update_game_window();
			redraw = false;
		}
		// Update display
        al_flip_display();
	}
}

int main() {
	// Init all allegro components
	if (!init_all()) {
		destroy_all();
		return -1;
	}
	// Load images
	load_imgs();
	// Call mainwindow
	show_mainwindow();
	// Call waiting window
	wait_time = 15;
	show_players_window();
	// Call game window
	show_game_window();
	// Delete all components
	destroy_all();

	return 0;
}