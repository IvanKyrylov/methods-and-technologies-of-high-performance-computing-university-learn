package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net"
	"net/rpc"
)

var command = map[string]string{
	"Да":      "unnamed_lo.png",
	"Литания": "litania.png",
	"Ересь":   "jebaited.png",
}

type Listener int
type Reply struct {
	Data []byte
}

func main() {
	addy, err := net.ResolveTCPAddr("tcp", "0.0.0.0:12345")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("Дух машина внемлит твоим словам!")
	inbound, err := net.ListenTCP("tcp", addy)
	if err != nil {
		log.Fatal(err)
	}
	listener := new(Listener)
	rpc.Register(listener)
	rpc.Accept(inbound)
}

func (l *Listener) CommandControl(line []byte, reply *Reply) error {
	source := string(line)
	// на основании полученных данных получаем из словаря изображение
	target, ok := command[source]
	if !ok { // если данные не найдены в словаре
		target = "imagesom.png"
	}
	// выводим на консоль сервера диагностическую информацию
	fmt.Println(source, "-", target)
	*reply = Reply{coder(target)}
	return nil
}

func coder(str string) []byte {
	var img64 []byte
	img64, _ = ioutil.ReadFile("./img/" + str)
	return img64
}
