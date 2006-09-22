svgfiles := $(wildcard *header.svg)
pngfiles := $(subst .svg,.png,$(svgfiles))
graypng := $(subst header.png,header-gray.png,$(pngfiles))
orangepng := $(subst header.png,header-orange.png,$(pngfiles))
redpng := $(subst header.png,header-red.png,$(pngfiles))
bluepng := $(subst header.png,header-blue.png,$(pngfiles))
greenpng := $(subst header.png,header-green.png,$(pngfiles))
yellowpng := $(subst header.png,header-yellow.png,$(pngfiles))



all: ${graypng}  ${orangepng}  ${redpng}  ${bluepng}  ${greenpng}  ${yellowpng} border-n.png border-s.png border-w.png border-e.png border-ne.png border-nw.png border-sw.png border-se.png

clean:
	rm -f $(pngfiles) ${graypng} ${orangepng}  ${redpng}  ${bluepng}  ${greenpng}  ${yellowpng} border-n.png border-s.png border-w.png border-e.png border-ne.png border-nw.png border-sw.png border-se.png

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

border-ne.png: border.png
	convert $< -crop 50x50+658+0 $@

border-nw.png: border.png
	convert $< -crop 50x50+0+0 $@

border-se.png: border.png
	convert $< -crop 50x50+658+507 $@

border-sw.png: border.png
	convert $< -crop 50x50+0+507 $@

border-s.png: border.png
	convert $< -crop 50x50+50+507 $@

border-n.png: border.png
	convert $< -crop 50x50+50+0 $@

border-w.png: border.png
	convert $< -crop 50x50+0+50 $@

border-e.png: border.png
	convert $< -crop 50x50+658+50 $@

#border.png -crop 50x50+658+0  border-ne.png
#border.png -crop 50x50+658+507  border-se.png
#border.png -crop 50x50+0+507  border-sw.png
#border.png -crop 50x50+50+507  border-s.png
#border.png -crop 50x50+50+0  border-n.png
#border.png -crop 50x50+658+50  border-e.png
#border.png -crop 50x50+0+50  border-w.png
#border.png -crop 50x50+0+0  border-nw.png
