svgfiles := $(wildcard *header.svg)
pngfiles := $(subst .svg,.png,$(svgfiles))
graypng := $(subst header.png,header-gray.png,$(pngfiles))
orangepng := $(subst header.png,header-orange.png,$(pngfiles))
redpng := $(subst header.png,header-red.png,$(pngfiles))
bluepng := $(subst header.png,header-blue.png,$(pngfiles))
greenpng := $(subst header.png,header-green.png,$(pngfiles))
yellowpng := $(subst header.png,header-yellow.png,$(pngfiles))



all: ${graypng}  ${orangepng}  ${redpng}  ${bluepng}  ${greenpng}  ${yellowpng}

clean:
	rm -f $(pngfiles) ${graypng} ${orangepng}  ${redpng}  ${bluepng}  ${greenpng}  ${yellowpng}


%header.png: %header.svg
	inkscape --export-png=$@ $<

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
