# Detect the host-os
# See http://stackoverflow.com/questions/714100/os-detecting-makefile
OS=$(shell uname -s)
# Darwin/Linux
rootdir = $(realpath .)

%.html : %.adoc
	asciidoctor -a icons=font -a toc=left -a toclevels=2 -a sectnums -a source-highlighter=highlight.js $<

# find all .adoc files in this project - ignores paths withs spaces
# since make does not handle them well
ADOC_SOURCES=$(shell find . -type f -name '*.adoc'|grep -v ' ')

# list all html and pdf files that can be produced
ADOC_HTML=$(ADOC_SOURCES:.adoc=.html)
ADOC_PDF=$(ADOC_SOURCES:.adoc=.pdf)

# first, default goal is to make html
html: $(ADOC_HTML)

# check for suspicious links
check-adoc-links:
# print stage info in blue
	@printf "\033[0;34mchecking suspicious links\033[0m\n"
	@grep -nire "link:.*.adoc" * --include "*.adoc" --color=auto || true

# check for filenames other than .adoc
check-adoc-extensions:
	@printf "\033[0;34mchecking wrong adoc extensions\033[1;31m\n"
ifeq ($(OS), Darwin)
	@find -E * -regex ".*\.(ad|asc|asciidoc)"
else
	@find * -regextype egrep -regex ".*\.(ad|asc|asciidoc)"
endif
# reset color to normal
	@printf "\033[0m"

# check for diagrams to be rendered using asciidoctor-diagram
check-diagram-includes:
	@printf "\033[0;34mchecking included diagrams for asciidoctor-diagram (currently unsupported)\033[0m\n"
	@grep -nirE "\[.*(actdiag|blockdiag|ditaa|graphviz|meme|mermaid|nwdiag|packetdiag|plantuml|rackdiag|seqdiag|shaape|wavedrom).*\]" * --include "*.adoc" --color=auto

# check for explicitly defined attributes
check-attributes:
	@printf "\033[0;34mchecking explicit attribute definitions\033[0m\n"
	@grep -nirE ":(toc|icons):" * --include "*.adoc" --color=auto

# generic check command
check: check-adoc-links check-adoc-extensions check-diagram-includes check-attributes

remove-html:
	find * -iname "*.html" -exec rm "{}" ";"

# remove pdf and html files
remove: remove-html remove-pdf
