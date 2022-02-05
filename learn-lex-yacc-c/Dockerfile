FROM gcc:latest AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN apt-get update -qq
RUN apt-get install -y bison flex
RUN autoreconf --install
RUN ./configure
RUN make
RUN make check

FROM ubuntu:latest
RUN mkdir /app
COPY --from=build /project/src/calc /app/calc
WORKDIR /app
CMD "./calc"
