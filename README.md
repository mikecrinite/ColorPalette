# ColorPalette
A simple app that allows a user to either take a photo, select one from the gallery, or enter a hex color code, and then creates a color palette based on the user's selection.

Once the color is determined, the app displays its hex code, along with the name of the closest known color (selected from wikipedia's List Of Colors)

The app also contains the full list of wikipedia colors. They are listed with their name and hex code and sorted by hex code. Unfortunately, there is no way to one-dimensionally sort colors that makes sense to the human eye, so some may seem out of order.
The colors can be displayed against a white or black background, toggleable by tapping the background.

### Planned features
* Scrape Wikipedia's color page regularly for a full list of colors automatically (currently, it only does this when a python script is run manually)
* Save a color palette for future use
* More accurate color palettes
