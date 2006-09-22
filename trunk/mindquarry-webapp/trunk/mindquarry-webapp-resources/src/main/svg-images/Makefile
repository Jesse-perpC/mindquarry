svgfiles := $(wildcard *header.svg)
pngfiles := $(subst .svg,.png,$(svgfiles))
graypng := $(subst header.png,header-gray.png,$(pngfiles))
orangepng := $(subst header.png,header-orange.png,$(pngfiles))
redpng := $(subst header.png,header-red.png,$(pngfiles))
bluepng := $(subst header.png,header-blue.png,$(pngfiles))
greenpng := $(subst header.png,header-green.png,$(pngfiles))
yellowpng := $(subst header.png,header-yellow.png,$(pngfiles))



all: ${graypng}  ${orangepng}  ${redpng}  ${bluepng}  ${greenpng}  ${yellowpng} border-n.gif border-s.gif border-w.gif border-e.gif border-ne.gif border-nw.gif border-sw.gif border-se.gif

clean:
	rm -f $(pngfiles) ${graypng} ${orangepng}  ${redpng}  ${bluepng}  ${greenpng}  ${yellowpng} border-n.gif border-s.gif border-w.gif border-e.gif border-ne.gif border-nw.gif border-sw.gif border-se.gif

pngs:	all
	rm -f $(pngfiles)

%header.png: %header.svg
	inkscape -D --export-png=$@ $<

%header-orange.png: %header.png
	convert $< -crop 1x70+300+0 $@

%header-gray.png: %header.png
	convert $< -crop 1x70+1+0 $@

%header-red.png: %header.png
	convert $< -crop 1x70+450+0 $@

%header-blue.png: %header.png
	convert $< -crop 1x70+600+0 $@

%header-green.png: %header.png
	convert $< -crop 1x70+750+0 $@

%header-yellow.png: %header.png
	convert $< -crop 1x70+900+0 $@

border.png: border.svg
	inkscape -D --export-png=$@ $<

border-ne.gif: border.png Makefile
	convert $< -crop 50x50+658+0 +repage $@

border-nw.gif: border.png Makefile
	convert $< -crop 50x50+0+0 +repage $@

border-se.gif: border.png Makefile
	convert $< -crop 50x50+658+507 +repage $@

border-sw.gif: border.png Makefile
	convert $< -crop 50x50+0+507 +repage $@

border-s.gif: border.png Makefile
	convert $< -crop 50x50+50+507 +repage $@

border-n.gif: border.png Makefile
	convert $< -crop 50x50+50+0 +repage $@

border-w.gif: border.png Makefile
	convert $< -crop 50x50+0+50 +repage $@

border-e.gif: border.png Makefile
	convert $< -crop 50x50+658+50 +repage $@
