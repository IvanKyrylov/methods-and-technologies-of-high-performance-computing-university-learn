package main

import (
	"bytes"
	"fmt"
	"image"
	"image/png"
	"log"
	"net/rpc"
	"os"
	"time"

	"github.com/faiface/beep"

	"github.com/faiface/beep/mp3"
	"github.com/faiface/beep/speaker"
	"github.com/kevin-cantwell/dotmatrix"
)

type Reply struct {
	Data []byte
}

func main() {
	conn, err := rpc.Dial("tcp", "localhost:12345")
	if err != nil {
		fmt.Println(err)
		return
	}
	var check bool = false
	defer conn.Close()
	for {
		var source string
		if !check {
			fmt.Println("Помолиться Духу Машине? Да/Нет")
			_, err := fmt.Scan(&source)
			if err != nil {
				fmt.Println("Дух машина не слышит вас.", err)
				continue
			}
			if source == "Да" {
				check = true
				f, err := os.Open("Children_of_the_Omnissiah.mp3")
				if err != nil {
					log.Fatal(err)
				}
				streamer, format, err := mp3.Decode(f)
				if err != nil {
					log.Fatal(err)
				}
				defer streamer.Close()
				speaker.Init(format.SampleRate, format.SampleRate.N(time.Second/10))

				speaker.Play(beep.Loop(-1, streamer))
			} else {
				if source == "Нет" {
					break
				} else {
					continue
				}
			}
		} else {
			fmt.Print("Что скромный слуга Омниссии хочет получить от Духа машины: ")
			_, err = fmt.Scan(&source)
			if err != nil {
				fmt.Println("Дух машина не слышит вас.", err)
				continue
			}
		}

		var reply Reply
		err = conn.Call("Listener.CommandControl", []byte(source), &reply)
		if err != nil {
			log.Fatal(err)
		}
		img, err := png.Decode(bytes.NewReader(reply.Data))
		Encode(img)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Println()
	}
}

func Encode(img image.Image) error {
	return dotmatrix.Print(os.Stdout, img)
}
