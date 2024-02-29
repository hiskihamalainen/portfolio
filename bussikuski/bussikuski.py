"""
A program for a drinking game called "Bussikuski".
"""


from tkinter import *

from random import randint

# Database for all the possible spots of the player backgrounds.
PLAYER_SPOT_POSITION = [(3, 9), (12, 9), (0, 5), (15, 5), (0, 3), (15, 3)]

# Database of the card image names.
IMAGE_FILES = ["2_of_clubs.gif", "2_of_diamonds.gif", "2_of_hearts.gif",
               "2_of_spades.gif", "3_of_clubs.gif", "3_of_diamonds.gif",
               "3_of_hearts.gif", "3_of_spades.gif", "4_of_clubs.gif",
               "4_of_diamonds.gif", "4_of_hearts.gif", "4_of_spades.gif",
               "5_of_clubs.gif", "5_of_diamonds.gif", "5_of_hearts.gif",
               "5_of_spades.gif", "6_of_clubs.gif", "6_of_diamonds.gif",
               "6_of_hearts.gif", "6_of_spades.gif", "7_of_clubs.gif",
               "7_of_diamonds.gif", "7_of_hearts.gif", "7_of_spades.gif",
               "8_of_clubs.gif", "8_of_diamonds.gif", "8_of_hearts.gif",
               "8_of_spades.gif", "9_of_clubs.gif", "9_of_diamonds.gif",
               "9_of_hearts.gif", "9_of_spades.gif", "10_of_clubs.gif",
               "10_of_diamonds.gif", "10_of_hearts.gif", "10_of_spades.gif",
               "ace_of_clubs.gif", "ace_of_diamonds.gif", "ace_of_hearts.gif",
               "ace_of_spades.gif", "jack_of_clubs.gif",
               "jack_of_diamonds.gif", "jack_of_hearts.gif",
               "jack_of_spades.gif", "king_of_clubs.gif",
               "king_of_diamonds.gif", "king_of_hearts.gif",
               "king_of_spades.gif", "queen_of_clubs.gif",
               "queen_of_diamonds.gif", "queen_of_hearts.gif",
               "queen_of_spades.gif"]


class Player:
    def __init__(self, name, cards, labels):
        """

        :param name: str, name of the player
        :param cards: list, list of the names of the cards the player has
        :param labels: dict, dictionary with the key being the label and
        payload being the image name attached to the label
        """
        self.__name = name
        self.__cards = cards
        self.__labels = labels
        self.__points = 6

    def get_name(self):
        """
        A method for returning the name of the player.

        :return: str, name of the player
        """
        return self.__name

    def get_cards(self):
        """
        A method for returning the list of the cards the player has.
        :return: list, list of the card names the player has
        """
        return self.__cards

    def get_points(self):
        """
        A method for returning the points the player has.

        :return: int, number of points the player has
        """
        return self.__points

    def get_labels(self):
        """
        A method for returning the dictionary of the labels attached to the
        player.

        :return: dict, dictionary of the labels where the label is the key
        and the name of the card image attached the label is the payload.
        """
        return self.__labels

    def get_card_name(self, name):
        """
        A method for returning the name/number of a specific card the
        player has.

        :param name: str, name of the card image
        :return: str, name/number of the card
        """
        fields = self.__labels[name].split("_")
        return fields[0]

    def get_card_full_name(self, label):
        """
        A method for returning the card image name attached to a specific
        label.

        :param label: the label being checked
        :return: str, name of the image file attached to the label
        """
        return self.__labels[label]

    def minus_point(self):
        """
        A method for subtracting one point from the player's points.
        """
        self.__points -= 1

    def del_specific_card(self, label):
        """
        A method for deleting a specific card from the list of cards the
        player has.

        :param label: label, the label of the card we want to delete
        """
        fields = self.__labels[label].split("_")
        name = fields[0]
        self.__cards.remove(name)

    def del_hand(self, image):
        """
        A method for deleting all the cards the player has in hand.
        :param image: image, the image file we want to configure the labels
        into, in this case being a blank image
        :return:
        """
        for label in self.get_labels():
            label.configure(image=image)
        self.__cards = []


class BussikuskiUi:

    def __init__(self):
        self.__main = Tk()

        # Set the window title and resolution
        self.__main.title("Bussikuski")
        self.__main.geometry("435x675")

        # An attribute for storing the main menu radiobuttons.
        self.__radio_buttons = []
        # An attribute for empty labels for better formatting.
        self.__empty_rows = []
        # An attribute for storing all the main menu UI elements for easier
        # management.
        self.__main_menu_elements = []

        # An attribute that changes depending on which radiobutton is pressed,
        # and thus shows how many player are selected.
        self.__player_amount = IntVar()

        # An attribute for storing the entry elements.
        self.__player_entrys = []
        # An attribute for storing the inputted player names.
        self.__player_names = []

        # An attribute for storing the bus image used  in the main menu.
        self.__bus_image = PhotoImage(file="bus.png").subsample(3, 3)

        # Create the empty rows used for formatting.
        self.empty_rows()

        # Create the main menu elements that dont need to be attributes.
        self.main_menu()

        # Create the buttons that initiates the player name submitting.
        self.__play_button = Button(self.__main, text="PLAY",
                                    foreground="blue", background="lightgrey",
                                    command=self.play)
        self.__play_button.grid(row=9, column=7)
        self.__main_menu_elements.append(self.__play_button)

        # Create the button that quits the program.
        self.__quit_button = Button(self.__main, text="QUIT",
                                    foreground="red", background="lightgrey",
                                    command=self.quit)
        self.__quit_button.grid(row=9, column=3)
        self.__main_menu_elements.append(self.__quit_button)

        # Create the elements that appear when names should be inputted,
        # but don't grid the button yet.
        self.__enter_button = Button(self.__main, text="Enter players",
                                     command=self.check_players)
        self.__main_menu_elements.append(self.__enter_button)

        self.__error_label = Label(self.__main, text="")
        self.__error_label.grid(row=12, column=4, columnspan=3)
        self.__main_menu_elements.append(self.__error_label)

        # Create the two buttons that bring up game instructions.
        self.__manual_finnish = Button(self.__main, text="Suomi",
                                       command=self.openmanual_finnish)
        self.__manual_finnish.grid(row=9, column=5)
        self.__main_menu_elements.append(self.__manual_finnish)

        self.__manual_english = Button(self.__main, text="English",
                                       command=self.openmanual_english)
        self.__manual_english.grid(row=10, column=5)
        self.__main_menu_elements.append(self.__manual_english)

        # Create two attributes that store the game instruction written
        # on outside text files.
        self.__finnish_instructions = open("bussikuski_suomi.txt",
                                           mode="r", encoding="utf8").read()
        self.__english_instructions = open("bussikuski_englanti.txt",
                                           mode="r", encoding="utf8").read()

        # Configure the radiobuttons.
        self.buttons()

        # An attribute for storing the player objects.
        self.__player_list = []
        # An attribute for storing the pyramid card labels.
        self.__label_list = []
        # An attribute for storing the indexes of the used cards.
        self.__used_cards = []
        # An attribute for keeping track on how many cards have been flipped.
        self.__flipped_cards = 0
        # An attribute for storing the losers of the base game.
        self.__list_of_losers = []
        # An attribute for storing an image name to be used in between
        # methods.
        self.__image_name = ""
        # An attribute for storing labels that have been flipped in the
        # bussikuski section.
        self.__bussikuski_labels = []

        # An attribute for storing the list of card image filenames from the
        # databe.
        self.__image_list = IMAGE_FILES
        # An attribute for storing both the image filenames AND the actual
        # images
        self.__card_images = {}
        # An attribute for storing the actual images
        self.__card_image_list = []

        self.__back_image = PhotoImage(file="back.gif").subsample(6, 6)
        self.__blank_image = PhotoImage(file="blank.gif").subsample(6, 6)
        self.__white_blank_image = PhotoImage(file="white_blank.gif").\
            subsample(6, 6)

        # Add the actual images to the list created before.
        for image in self.__image_list:
            new_image = (PhotoImage(file=image)).subsample(6, 6)
            self.__card_image_list.append(new_image)

        # Add both the filenames and the actual images to the dictinary
        # created before.
        for index in range(len(self.__image_list)):
            name = self.__image_list[index]
            self.__card_images[self.__card_image_list[index]] = name

        # Create all the buttons used in the game UI, but don't grid them yet.
        self.__flip_button = Button(self.__main, text="Turn a card",
                                    command=self.pyramid_card_flip)
        self.__quit_button = Button(self.__main, text="Quit",
                                    command=self.quit)
        self.__retry_button = Button(self.__main, text="Retry",
                                     command=self.retry)
        self.__big_label = Label(self.__main, text="RYYS", background="pink",
                                 font=("Arial", 24), pady=25, height=3)

        # Run the UI object.
        self.__main.mainloop()

    def main_menu(self):
        """
        A method for creating all the non-attribute UI elements of the main
        menu.
        """
        # Create a label that shows the name of the game.
        game_title = Label(self.__main, text="BUSSIKUSKI",
                           font=("fixedsys", 32))
        game_title.grid(row=1, column=3, columnspan=5)
        self.__main_menu_elements.append(game_title)

        # Create a label that shows a bus image.
        bus_label = Label(self.__main, image=self.__bus_image)
        bus_label.grid(row=2, column=1, columnspan=9)
        self.__main_menu_elements.append(bus_label)

        # Create a label asking the user to click on how many players are
        # playing.
        playerlabel = Label(self.__main,
                            text="How many going for a ride?")
        playerlabel.grid(row=3, column=5, columnspan=1)
        self.__main_menu_elements.append(playerlabel)

        # Create a label that shows the user the buttons for instructions.
        instructions = Label(self.__main, text="Instructions:")
        instructions.grid(row=8, column=5)
        self.__main_menu_elements.append(instructions)

    def buttons(self):
        """
        A method for creating all the radio buttons in a loop.
        """
        # Create six radiobuttons, that when clicked on, communicate how many
        # players are playing the game.
        for number in range(3):
            new_button = Radiobutton(self.__main, text=f"{number + 2}",
                                     padx=20, variable=self.__player_amount,
                                     value=number + 2, indicatoron=False)
            self.__radio_buttons.append(new_button)
            new_button.grid(row=4, column=(number * 2) + 3)

        for number in range(2):
            new_button = Radiobutton(self.__main, text=f"{number + 5}",
                                     padx=20, variable=self.__player_amount,
                                     value=number + 5, indicatoron=False)
            self.__radio_buttons.append(new_button)
            new_button.grid(row=6, column=(number * 2) + 4)

    def empty_rows(self):
        """
        A method for creating all the empty rows and columns for formatting.
        """
        # Create some empty rows and columns to format the main menu UI grid
        # better.
        for number in range(12):
            new_row = Label(self.__main, text="")
            self.__empty_rows.append(new_row)
            new_row.grid(row=number, column=0, columnspan=9)

        emptycolumn1 = Label(self.__main, text="", padx=15)
        emptycolumn1.grid(row=0, column=0, columnspan=3, rowspan=8)
        self.__main_menu_elements.append(emptycolumn1)

        emptycolumn2 = Label(self.__main, text="", padx=15)
        emptycolumn2.grid(row=0, column=8, columnspan=3, rowspan=8)
        self.__main_menu_elements.append(emptycolumn2)

    def openmanual_finnish(self):
        """
        A method that happens when the button for Finnish instructions is
        pressed.
        """
        # Create a pop-up window
        instructions = Toplevel(self.__main)
        instructions.geometry("750x700")
        instructions.title("Ohjeet")

        # Create a label showing the text in a database to the pop-up.
        Label(instructions,
              text=f"{self.__finnish_instructions}").place(x=150, y=80)

    def openmanual_english(self):
        """
        A method that happens when the button for English instructions is
        pressed.
        """
        # Create a pop-up window
        instructions = Toplevel(self.__main)
        instructions.geometry("750x700")
        instructions.title("Instructions")

        # Create a label showing the text in a database to the pop-up.
        Label(instructions,
              text=f"{self.__english_instructions}").place(x=150, y=80)

    def get_players(self):
        """
        A method for returning the amount of players selected with the
        radio buttons.
        :return: int, the value of the pressed radio button, A.K.A the number
        of players
        """
        return self.__player_amount.get()

    def player_names(self):
        """
        A method that happens when the play button is pressed while a radio
        button is also pressed down, creating the necessary entry elements.
        """
        # Check how many players are playing.
        player_amount = self.get_players()

        # Create as many labels as there are players.
        for number in range(player_amount):
            player_entry = Entry()
            player_entry.grid(row=number + 12, column=5)
            self.__player_entrys.append(player_entry)

        # Configure the enter button to actually show something.
        self.__enter_button.configure(text="Enter",
                                      command=self.check_players)

        # Grid the error label.
        self.__error_label.grid(row=player_amount + 13)
        self.__enter_button.grid(row=player_amount + 12, column=5)

    def check_players(self):
        """
        A method that happens when the enter button is pressed.
        """
        # Create two empty list variables used for storing the entry inputs.

        self.__player_names = []

        # Check all the inputs in the previously created entry elements.
        for entry in self.__player_entrys:
            # If an input isn't blank, append it to the list created earlier.
            if entry.get() != "":
                self.__player_names.append(entry.get())
            # If it is, change the error label to show and error and stop the
            # method.
            else:
                self.__error_label.configure(
                    text="Names must consist of letters")
                return

        # Check all the inputs.
        for index in range(len(self.__player_names)):
            # Create a bookkeeping variable.
            similarity = 0
            # For each name on the list, check if there are more of the same
            # name.
            for value in range(len(self.__player_names)):
                # If there is, add one to the variable
                if self.__player_names[index] == self.__player_names[value]:
                    similarity += 1

            # If there are two or more of the same name, show an error and
            # stop the method
            if similarity > 1:
                self.__error_label.configure(text="Names must be different")
                return

        self.gridding()
        self.pyramid_card_label_creation()
        self.player_card_label_creation()

    def play(self):
        """
        A method for when the play button is pressed.
        """
        # If there are entry elements from before, destroy them.
        if len(self.__player_entrys) > 0:
            for entry in self.__player_entrys:
                entry.destroy()

        # If a radio button isn't pressed, nothing happens.
        if self.get_players() > 0:
            self.player_names()

    def gridding(self):
        """
        A method for hiding the main menu UI elemenents and bringing up the
        bussikuski UI elements.
        """
        # First hide all the main menu UI elements.
        for element in self.__main_menu_elements:
            element.grid_forget()
        for element in self.__radio_buttons:
            element.grid_forget()
        for element in self.__empty_rows:
            element.grid_forget()
        for element in self.__player_entrys:
            element.grid_forget()

        # Change the window size to full HD.
        self.__main.geometry("1920x1080")

        # Grid all the bussikuski UI elements.
        self.__flip_button.grid(row=9, column=10)
        self.__quit_button.grid(row=12, column=10)
        self.__retry_button.grid(row=11, column=10, sticky=S)
        self.__big_label.grid(row=0, column=6, rowspan=3, columnspan=9,
                              sticky=E + W + N + S)

    def player_creation(self, name, card_list, labels):
        """
        A method for creating the player-objects at the start of each game
        and appending them to an attribute for later use.

        :param name: str, name of the player
        :param card_list: list, list of the card image names the player has
        :param labels: dict, dictionary of the cards the player has, where
        the key is the label, and the payload the image/card name
        """
        # Create an empty local list variable to store the card names in.
        list_of_cards = []

        # Split the card image names and append the first to the list created
        # before, it being the number of the card (i.e. "eight" or "jack").
        for index in range(6):
            fields = card_list[index].split("_")
            list_of_cards.append(fields[0])

        # Create a player object with the three attributes.
        player = Player(name, list_of_cards, labels)
        # Add the created player object to a list.
        self.__player_list.append(player)

    def random_image(self):
        """
        A method for returning a randomized card from the list of cards.

        :return: str, name of the randomized card
        """
        # Get a random number used as an index.
        card_index = random(self.__used_cards)
        # Add the random number to the list of already used numbers.
        self.__used_cards.append(card_index)
        # Return the randomized image.
        return self.__card_image_list[card_index]

    def pyramid_card_label_creation(self):
        """
        A method for creating the labels for the future pyramid cards.
        """
        # Create the different pyramid labels, used for showing the cards in
        # a loop. Also add the the created labels into a list for later use.
        # Labels are created from left to right and bottom to top in a
        # pyramid shape.
        for number in range(5):
            new_label = Label(self.__main, image=self.__back_image)
            self.__label_list.append(new_label)
            new_label.grid(row=7, column=number * 2 + 6)
        for number in range(4):
            new_label = Label(self.__main, image=self.__back_image)
            self.__label_list.append(new_label)
            new_label.grid(row=6, column=number * 2 + 7)
        for number in range(3):
            new_label = Label(self.__main, image=self.__back_image)
            self.__label_list.append(new_label)
            new_label.grid(row=5, column=number * 2 + 8)
        for number in range(2):
            new_label = Label(self.__main, image=self.__back_image)
            self.__label_list.append(new_label)
            new_label.grid(row=4, column=number * 2 + 9)
        # Just wanted to make this look uniform, thanks OCD :).
        for number in range(1):
            new_label = Label(self.__main, image=self.__back_image)
            self.__label_list.append(new_label)
            new_label.grid(row=3, column=10)

    def player_card_label_creation(self):
        """
        A method for creating the player spot and name labels and the
        labels for the future player cards.
        """
        # First create the backgrounds for each player.
        for number in range(len(self.__player_names)):
            player_cards = Label(self.__main, pady=150, padx=150,
                                 background="pink")

            # Take the precise positions of the backgrounds from a database,
            # because we have a max number of players.
            x, y = PLAYER_SPOT_POSITION[number]
            player_cards.grid(row=y, column=x, columnspan=6)

            # Create an empty list and a dictionary for the labels and
            # cards the backgrounds will house.
            list_of_card_names = []
            player_card_labels = {}

            # Give 6 cards for each player.
            for value in range(6):
                # Get a randomized image.
                image_name = self.random_image()
                # Append the image name to list from earlier.
                list_of_card_names.append(self.__card_images[image_name])

                # Create a label under the before created background with
                # its image being the randomized one from earlier.
                card = Label(player_cards, image=image_name)
                card.grid(row=y, column=x + value)
                # Bind an event widget to the created card label.
                card.bind("<Button-1>", self.player_card_flip)

                # Add the labels into the dictionary from earlier, with
                # the image being the payload, for making some functions
                # later on easier.
                player_card_labels[card] = self.__card_images[image_name]

            # Create the player objects with the variables created before.
            self.player_creation(self.__player_names[number],
                                 list_of_card_names, player_card_labels)

            # Make a name label under each players cards, housing their names.
            name_label = Label(self.__main,
                               text=f"{self.__player_list[number].get_name()}",
                               font=36)
            name_label.grid(row=y + 1, column=x, sticky=N,
                            columnspan=6)

    def player_card_flip(self, event):
        """
        A method that gets binded to the player cards. It turns them around
        when clicked.

        :param event: the specified event, in this case, pressing M1.
        """
        # This event widget changes the image on the clicked label to
        # picture of the backside of a card.
        label = event.widget
        for player in self.__player_list:
            # Go through each players label lists to find the player whose
            # label was clicked.
            if label in player.get_labels():
                # Check if the turned card from the pyramid (self.__image_name)
                # is the same name/number as the card being clicked on.
                if self.__image_name == player.get_card_name(label):
                    # If it is, change the image to the picture of the
                    # backside of a card,
                    label.configure(image=self.__back_image)
                    # subtract one point from the player,
                    player.minus_point()
                    # delete the turned card from the player's attributes and
                    player.del_specific_card(label)
                    # unbind the event widget from the clicked label.
                    label.unbind("<Button-1>")

    def pyramid_card_flip(self):
        """
        A method for flipping the cards of the pyramid, one card at a time.
        """
        # Get a randomized image.
        value = random(self.__used_cards)
        self.__used_cards.append(value)

        # Change the image of the current card to the randomized image.
        # We achieve this by using the amount of flipped cards as the index
        # of the list of created pyramid card labels.
        self.__label_list[self.__flipped_cards]\
            .configure(image=self.__card_image_list[value])
        self.__flipped_cards += 1

        # Split the card image name, to get the name/number of the card.
        fields = self.__card_images[self.__card_image_list[value]].split("_")
        self.__image_name = fields[0]

        # Initiate the function for counting how many drinks should be given
        # out.
        self.drink_giving(self.__image_name)

        # If the amount of flipped cards is 15, it means the whole pyramid
        # is flipped, and thus the game should be decided. Configure the
        # flip button to initiate the game ending function.
        if self.__flipped_cards == 15:
            self.__flip_button.configure(text="Game end",
                                         command=self.game_end)

    def drink_giving(self, image_name):
        """
        A method for configuring the big label to show how many drinks
        should be given out and by whom.
        :param image_name: str, name of the flipped card from the pyramid
        """
        # Create an empty local list variable for later use.
        player_list = []

        # Go through each player.
        for player in self.__player_list:
            # If "image_name" matches an entry in the players hand,
            # we append that player's name to list created before as many
            # times as the card appears in the player's hand.
            for number in range(player.get_cards().count(image_name)):
                player_list.append(player.get_name())

        # Create an empty local dictionary for later use.
        player_dict = {}

        # Go through each entry of the list created before.
        for player in player_list:
            # Baseline amount is zero.
            if player not in player_dict:
                player_dict[player] = 0

            # If the flipped card is in the first row, each mention adds the
            # amount by 1.
            if self.__flipped_cards < 6:
                player_dict[player] += 1
            # If the flipped card is in the second row, each mention adds the
            # amount by 2.
            elif self.__flipped_cards < 10:
                player_dict[player] += 2
            # If the flipped card is in the third row, each mention adds the
            # amount by 3.
            elif self.__flipped_cards < 13:
                player_dict[player] += 3
            # If the flipped card is in the fourth row, each mention adds the
            # amount by 4.
            elif self.__flipped_cards < 15:
                player_dict[player] += 4
            # If the flipped card is in the last row, each mention adds the
            # amount by 5.
            else:
                player_dict[player] += 5

        # Create an empty string for later use.
        string = ""

        # Go through each entry of the dictionary created before and create
        # the correct string to show on the big label.
        for player in player_dict:
            # If the payload is 1, then the amount is not plural,
            if player_dict[player] == 1:
                string += f"{player} may give out " \
                          f"{player_dict[player]} drink \n"
            # if it's bigger than 1, then the amount is plural.
            else:
                string += f"{player} may give out " \
                          f"{player_dict[player]} drinks \n"

        # Configure the big label to show the string created just now.
        self.__big_label.configure(text=string)

        # If there are no matches with the players' hands, then the label
        # should show the default text.
        if not player_list:
            self.__big_label.configure(text="RYYS")

    def game_end(self):
        """
        A method that gets binded to the flip button that's used
        for when the game ends. We check who's the winner, if a tie
        should happen and how we proceed.
        """
        # Create an empty local list variable to store the amount of points
        # each player has at the end of the game.
        list_of_points = []

        # Go through each player and add their points into the list created
        # just now.
        for player in self.__player_list:
            list_of_points.append(player.get_points())

        # Sort the points.
        sorted_points = sorted(list_of_points)

        # Go through each player.
        for player in self.__player_list:
            # If the player has the least amount of points, add their name
            # into a list.
            if sorted_points[-1] == player.get_points():
                self.__list_of_losers.append(player)

        # If there are more than one losers:
        if len(self.__list_of_losers) > 1:
            # 1. Go through each player,
            for player in self.__player_list:
                # and if the the player isn't a loser,
                if player not in self.__list_of_losers:
                    # delete their cards.
                    player.del_hand(self.__white_blank_image)

            # 2. Also turn all the pyramid cards, except for the one in the
            # middle, into white blanks.
            for index in range(15):
                if index != 10:
                    self.__label_list[index].configure(
                        image=self.__blank_image)

            # 3. Create an empty string for later use.
            name = ""

            # 4. Make the string that's used later in configurating the big
            # label to show the losers' names.
            for index in range(len(self.__list_of_losers)):
                # If the index is the last, we add an "and" to the start of the
                # name.
                if index == len(self.__list_of_losers)-1:
                    name += f" and {self.__list_of_losers[index].get_name()}"
                # If the index isn't the first, we add a comma to the start
                # of the name.
                elif index != 0:
                    name += f", {self.__list_of_losers[index].get_name()}"
                # If the index is the first, we don't add anything.
                else:
                    name += f"{self.__list_of_losers[index].get_name()}"

            # Reset the attributes that store the names of the used cards
            # and the amount of flipped cards, since we need them later.
            self.__used_cards = []
            self.__flipped_cards = 0

            # Get a random picture.
            value = random(self.__used_cards)
            self.__used_cards.append(value)

            # Change the middle pyramid card label image into the randomized
            # picture.
            self.__label_list[10].configure(
                image=self.__card_image_list[value])

            # Get the name/number of the middle card image.
            fields = self.__card_images[
                self.__card_image_list[value]].split("_")
            self.__image_name = fields[0]

            # Go through each player.
            for player in self.__player_list:
                # If the player is a loser,
                if player in self.__list_of_losers:
                    # go through their card remaining cards
                    for label in player.get_labels():
                        # and add them into the list of used cards for the tie
                        # resolving.
                        index = self.__image_list.index(
                            player.get_card_full_name(label))
                        self.__used_cards.append(index)

            # Configure the big label to show the result of the game.
            self.__big_label.configure(text=f"Tie between players {name}")
            # Configure the flip button to change the card in the middle.
            self.__flip_button.configure(text="New card", command=self.tie,
                                         state="normal")

        # If there's only one loser,
        else:
            # configure the flip button to initialize the bussikuski section
            self.__flip_button.configure(text="Let's drive!",
                                         command=self.bussikuski)
            # and configure the big label to congratule the "winner".
            self.__big_label.\
                configure(text=f"{self.__list_of_losers[0].get_name()} "
                               f"becomes bussikuski! \n"
                               f"Congrats on your bus driving license! :)")

    def tie(self):
        """
        A method that gets binded to the flip button that is used
        for resolving a tie.
        """
        # Create an empty local dictionary to store the cards each loser has.
        loser_cards = {}

        # Go through each loser.
        for player in self.__list_of_losers:
            # If the amount of of cards in the player's hand isn't a key
            # in the dictionary, create and empty list as the payload of it.
            if len(player.get_cards()) not in loser_cards:
                loser_cards[len(player.get_cards())] = []

            # Append the player to list that matches the amount of cards
            # in their hand.
            loser_cards[len(player.get_cards())].append(player)

        # Create a variable that houses the players with the most cards in
        # their hand.
        last = sorted(loser_cards)[-1]

        # If there are multiple tied players for most cards in hand,
        if len(loser_cards[last]) > 1:
            # configure the middle cards to show a new image.
            value = random(self.__used_cards)
            self.__used_cards.append(value)

            fields = self.__card_images[
                self.__card_image_list[value]].split("_")
            self.__image_name = fields[0]

            self.__label_list[10].configure(
                image=self.__card_image_list[value])

        # If there is a singular player with the most cards in hand,
        else:
            # configure the flip button to initialize the
            # bussikuski section and
            self.__flip_button.configure(text="Let's drive!",
                                         command=self.bussikuski,
                                         state="normal")

            # configure the big label to show who "won".
            loser_name = loser_cards[last][0].get_name()
            self.__big_label.configure(text=f"{loser_name} "
                                       f"becomes bussikuski! \n"
                                       f"Congrats on your bus"
                                       f" driving license! :)")

        # If each card is used (incredibly rare to happen without just clicking
        # through them all), randomize the winner.
        if len(self.__used_cards) == 51:
            # Get a random number thats at most equal the number of losers.
            value = randint(0, len(self.__list_of_losers))

            # Configure the flip button to initialize the bussikuski section.
            self.__flip_button.configure(text="Let's drive!",
                                         command=self.bussikuski,
                                         state="normal")

            # Configure the big label to show who won.
            self.__big_label.configure(
                # Use the randomized number as the index of the list of losers.
                text=f"{self.__list_of_losers[value].get_name()} "
                     f"becomes bussikuski! \n"
                     f"Congrats on your bus driving license! :)")

    def bussikuski(self):
        """
        A method for starting up the bussikuski-section of the game.
        """
        # At the start of the bussikuski section, destroy each previously
        # created pyramid label.
        for number in range(15):
            self.__label_list[number].destroy()

        # Reset the attributes we need for the bussikuski section.
        self.__label_list = []
        self.__used_cards = []
        self.__flipped_cards = 0

        # Erase each players hands.
        for player in self.__player_list:
            player.del_hand(self.__blank_image)

        # Disable the flip button, we don't need it anymore.
        self.__flip_button.configure(state="disabled")

        # Create new pyramid card labels.
        self.pyramid_card_label_creation()

        # Bind the first row of the pyramid with an event widget, that turns
        # them.
        for index in range(5):
            self.__label_list[index].bind("<Button-1>", self.bussikuski_flip)

    def bussikuski_flip(self, event):
        """
        A method that gets binded to the pyramid cards. It flips them around
        and checks how we proceed.

        :param event: the specified event, in this case, pressing M1.
        """
        label = event.widget

        # Get a random card.
        value = random(self.__used_cards)
        self.__used_cards.append(value)

        # Change the clicked label's image into the randomized card.
        label.configure(image=self.__card_image_list[value])
        # Add the flipped label into a list, in order to keep track of the
        # flipped labels.
        self.__bussikuski_labels.append(label)

        # Add 1 to amount of flipped cards to keep track how many cards we
        # have flipped.
        self.__flipped_cards += 1

        # Initialize a function for binding and unbinding buttons.
        self.bussikuski_button_binding()

        # Change the attribute "self.__image_name" into the name of the last
        # flipped card, in order to check if it's a face card.
        fields = self.__card_images[self.__card_image_list[value]].split("_")
        self.__image_name = fields[0]

        # Initialize the function to check if the
        self.bussikuski_face_card_flip()

        # If 5 cards have been successfully flipped, then victory is achieved
        # and the game ends.
        if self.__flipped_cards == 5:
            self.__big_label.configure(text="Victory!")

    def bussikuski_button_binding(self):
        """
        A method for binding and unbinding the wanted pyramid cards.
        """
        # After each card turn, depending on how many cards have been turned,
        # unbind the current row and bind the next row. This way we can't
        # flip any cards we shouln't be able to.
        if self.__flipped_cards == 1:
            for index in range(5):
                self.__label_list[index].unbind("<Button-1>")
            for index in range(4):
                self.__label_list[5 + index].bind("<Button-1>",
                                                  self.bussikuski_flip)
        elif self.__flipped_cards == 2:
            for index in range(4):
                self.__label_list[5 + index].unbind("<Button-1>")
            for index in range(3):
                self.__label_list[9 + index].bind("<Button-1>",
                                                  self.bussikuski_flip)
        elif self.__flipped_cards == 3:
            for index in range(3):
                self.__label_list[9 + index].unbind("<Button-1>")
            for index in range(2):
                self.__label_list[12 + index].bind("<Button-1>",
                                                   self.bussikuski_flip)
        elif self.__flipped_cards == 4:
            for index in range(2):
                self.__label_list[12 + index].unbind("<Button-1>")
            for index in range(1):
                self.__label_list[14].bind("<Button-1>", self.bussikuski_flip)

        elif self.__flipped_cards == 5:
            # Just wanted to this look uniform too :)
            for index in range(1):
                self.__label_list[14].unbind("<Button-1>")

    def bussikuski_face_card_flip(self):
        """
        A method for checking if the turned card from the bussikuski-pyramid
        is a face card, and if it is, how we proceed.
        """
        # For each face card, check if the newest flipped card is the same
        # number. If it is, depending on the number of flipped cards,
        # configure the big label to show how many chugs should be taken.
        # Also reset the flipped cards amount and initialize the function
        # for going again from the start.
        # If the most recently flipped card isn't a face card, nothing happens.
        if self.__image_name == "jack":
            if self.__flipped_cards == 1:
                self.__big_label.configure(text=f"Take a chug! :) \n"
                                                f"Try again!")
            else:
                self.__big_label.configure(
                    text=f"Take {self.__flipped_cards} chugs! :) \n"
                         f"Try again!")

            self.__flipped_cards = 0
            self.bussikuski_try_again()

        elif self.__image_name == "queen":
            if self.__flipped_cards == 1:
                self.__big_label.configure(text=f"Take a chug! :) \n"
                                                f"Try again!")
            else:
                self.__big_label.configure(
                    text=f"Take {self.__flipped_cards} chugs! :) \n"
                         f"Try again!")

            self.__flipped_cards = 0
            self.bussikuski_try_again()

        elif self.__image_name == "king":
            if self.__flipped_cards == 1:
                self.__big_label.configure(text=f"Take a chug! :) \n"
                                                f"Try again!")
            else:
                self.__big_label.configure(
                    text=f"Take {self.__flipped_cards} chugs! :) \n"
                         f"Try again!")

            self.__flipped_cards = 0
            self.bussikuski_try_again()

        elif self.__image_name == "ace":
            if self.__flipped_cards == 1:
                self.__big_label.configure(text=f"Take a chug! :) \n"
                                                f"Try again!")
            else:
                self.__big_label.configure(
                    text=f"Take {self.__flipped_cards} chugs! :) \n"
                         f"Try again!")

            self.__flipped_cards = 0
            self.bussikuski_try_again()

    def bussikuski_try_again(self):
        """
        A method for configuring what happens when a face card is turned
        from the pyramid.
        """
        # First, unbind all the buttons so we can't flip anything after
        # flipping a face card.
        for index in range(15):
            self.__label_list[index].unbind("<Button-1>")

        # Then, after waiting 0,6 seconds to show what the flipped card was,
        # initialize the function for turning the flipped cards back to
        # face up.
        self.__main.after(600, self.bussikuski_card_turn)

    def bussikuski_card_turn(self):
        """
        A method for turning around all the cards of the bussikuski-pyramid,
        and thus resetting the pyramid.
        """
        # Configure every pyramid label to show the image of a card's backside.
        for label in self.__bussikuski_labels:
            label.configure(image=self.__back_image)

        # Bind the first 5 cards with the flipping event widget.
        for index in range(5):
            self.__label_list[index].bind("<Button-1>", self.bussikuski_flip)

        # Reset the variables we need for the bussikuski section.
        # Note that the variable that keeps track of used cards doens't get
        # reset, in order to keep the cards realistically randomized.
        self.__bussikuski_labels = []
        self.__flipped_cards = 0

    def retry(self):
        """
        A method for resetting the game board and starting a new game.
        """
        # Destroy all the pyramid card labels.
        for number in range(15):
            self.__label_list[number].destroy()

        # Reset all the attributes that are used.
        self.__player_list = []
        self.__label_list = []
        self.__used_cards = []
        self.__flipped_cards = 0
        self.__list_of_losers = []
        self.__image_name = ""
        self.__bussikuski_labels = []

        # Reset the flip button to its original state.
        self.__flip_button.configure(text="Turn a card", state="normal",
                                     command=self.pyramid_card_flip)

        # Reset the big label to its original state.
        self.__big_label.configure(text="RYYS")

        # Create all the card labels again.
        self.pyramid_card_label_creation()
        self.player_card_label_creation()

    def quit(self):
        """
        A method for quitting the program.
        """
        # Destroy the main UI object.
        self.__main.destroy()


def random(card_list):
    """
    A function that returns a random number from 0 to 51, if it hasn't already
    been used before.

    :param card_list: list, a list of indexed that have previously been used
    :return: int, a random number to be used as an index
    """
    while True:
        # Get a random number between 0 and 51, since there are 52 cards in
        # a deck of cards.
        card_index = randint(0, 51)
        # Repeat until we get a result not in the list.
        if card_index not in card_list:
            return card_index


def main():

    # Start up the UI object.
    BussikuskiUi()


if __name__ == "__main__":
    main()
