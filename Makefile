# This is a generic Makefile for generating various output files from asciidoc
# files using Asciidoctor
# make filename.html
# make filename.pdf
# make filename.reveal.html

# Detect the host-os
# See http://stackoverflow.com/questions/714100/os-detecting-makefile
ifeq ($(OS),Windows_NT)
	OS := Windows
else
	OS := $(shell uname -s)
endif
# Windows/Darwin/Linux

# Find the path of this Makefile when called from a subdirectory like:
# $ make -f ../../Makefile local-adocfile.html
# http://stackoverflow.com/questions/322936/common-gnu-makefile-directory-path
TOP:= $(abspath $(dir $(lastword $(MAKEFILE_LIST))))

# Commands
ASCIIDOCTOR = asciidoctor
ASCIIDOCTOR_PDF = asciidoctor-pdf

# Fonts and styles
FONTSDIR = $(TOP)/assets/fonts
#PFDFONTSDIR = -a pdf-fontsdir=$(FONTSDIR)
#HTMLSTYLE = -a stylesheet=$(TOP)/asciidoctor.css
#PDFSTYLE = $(PDFFONTSDIR) -a pdf-style=$(TOP)/asciidoctor.yml
#REVEALSTYLE = $(TOP)/revealjs/
EXTDIR = $(TOP)/../asciidoctor-extensions-lab/lib

# Build flags
DOCFLAGS    = -a icons=font -a toc=left -a toclevels=2 -a sectnums -a sectlinks
HTMLEXT     = -r $(EXTDIR)/protect-emails-postprocessor.rb -r $(EXTDIR)/navigation-toc-postprocessor.rb
HTMLFLAGS   = -a icons=font -a sectlinks $(HTMLSTYLE) -a source-highlighter=coderay
PDFFLAGS    = $(DOCFLAGS) $(PDFSTYLE) -a source-highlighter=coderay
#REVEALFLAGS = ...

# build .html files from .adoc files
%.html : %.adoc
	$(ASCIIDOCTOR) $(HTMLEXT) $(HTMLFLAGS) $<

# same, but for pdf
%.pdf: %.adoc
	$(ASCIIDOCTOR_PDF) $(PDFFLAGS) $<

# find all .adoc files in this project - ignores paths withs spaces
# since make does not handle them well
ADOC_SOURCES=$(shell find . -type f -name '*.adoc'|grep -v ' ')

# list all files that can be produced
ADOC_HTML   = $(ADOC_SOURCES:.adoc=.html)
ADOC_PDF    = $(ADOC_SOURCES:.adoc=.pdf)
ADOC_REVEAL = $(ADOC_SOURCES:.adoc=.reveal.html)

# first, default goal is to make html
default: html

.PHONY: html
html: $(ADOC_HTML)

.PHONY: all
all: html pdf reveal

.PHONY: pdf
pdf: $(ADOC_PDF)

.PHONY: reveal
reveal: $(ADOC_REVEAL)

# check for suspicious links
.PHONY: check-adoc-links
check-adoc-links:
# print stage info in blue
	@printf "\033[0;34mchecking suspicious links\033[0m\n"
	@grep -nire "link:.*.adoc" * --include "*.adoc" --color=auto || true

# check for filenames other than .adoc
.PHONY: check-adoc-extensions
check-adoc-extensions:
	@printf "\033[0;34mchecking wrong adoc extensions\033[1;31m\n"
ifeq ($(OS), Linux)
	@find * -type f -regextype egrep -regex ".*\.(ad|asc|asciidoc)"
else ifeq ($(OS), Darwin)
	@find -type f -E * -regex ".*\.(ad|asc|asciidoc)"
else
  $(error Operating system not supported)
endif
# reset color to normal
	@printf "\033[0m"

# check for diagrams to be rendered using asciidoctor-diagram
.PHONY: check-diagram-includes
check-diagram-includes:
	@printf "\033[0;34mchecking included diagrams for asciidoctor-diagram (currently unsupported)\033[0m\n"
	@grep -nirE "\[.*(actdiag|blockdiag|ditaa|graphviz|meme|mermaid|nwdiag|packetdiag|plantuml|rackdiag|seqdiag|shaape|wavedrom).*\]" * --include "*.adoc" --color=auto || true

# check for explicitly defined attributes
.PHONY: check-attributes
check-attributes:
	@printf "\033[0;34mchecking explicit attribute definitions\033[0m\n"
	@grep -nirE ":(toc|icons):" * --include "*.adoc" --color=auto || true

# check for adoc filesnames with spaces
.PHONY: check-spaces
check-spaces:
	@printf "\033[0;34mchecking for spaces in filenames\033[0m\n"
	@find . -type f -name '*.adoc'|grep ' ' || true

# generic check command
.PHONY: check
check: check-adoc-links check-adoc-extensions check-diagram-includes check-attributes check-spaces

# remove html files
.PHONY: clean
clean:
# pusing prune to exclude the build directory
ifeq ($(OS), Linux)
#	find $(TOP)/* -type f -regextype egrep -regex ".*\.(html|pdf)" -exec rm "{}" ";"
	find $(TOP)/* \
	-path $(TOP)/assets -prune -o \
	-type f -regextype egrep -regex ".*\.(html|pdf)" -exec rm "{}" ";"
else ifeq ($(OS), Darwin)
#	find $(TOP)/* -type f -E * -regex ".*\.(html|pdf)" -exec rm "{}" ";"
	find $(TOP)/* \
	-path $(TOP)/assets -prune -o \
	-type f -E * -regex ".*\.(html|pdf)" -exec rm "{}" ";"
else
  $(error Operating system not supported)
endif
