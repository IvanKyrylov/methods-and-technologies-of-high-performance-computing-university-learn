package main

import (
	"bytes"
	"fmt"
	"image"
	"image/png"
	"log"
	"net"
	"os"
	"time"

	"github.com/faiface/beep"

	"github.com/faiface/beep/mp3"
	"github.com/faiface/beep/speaker"
	"github.com/kevin-cantwell/dotmatrix"
)

func main() {
	conn, err := net.Dial("tcp", "127.0.0.1:4545")
	if err != nil {
		fmt.Println(err)
		return
	}
	var check bool = false
	defer conn.Close()
	for {
		var source string
		buff := make([]byte, 1024*64)
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

		// отправляем сообщение серверу
		if n, err := conn.Write([]byte(source)); n == 0 || err != nil {
			fmt.Println(err)
			return
		}
		// получем ответ

		_, err = conn.Read(buff)
		if err != nil {
			break
		}
		img, err := png.Decode(bytes.NewReader(buff))
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

func sound() {

}
