package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net"
)

var command = map[string]string{
	"Да":      "unnamed_lo.png",
	"Литания": "litania.png",
	"Ересь":   "jebaited.png",
}

func main() {
	listener, err := net.Listen("tcp", ":4545")
	if err != nil {
		log.Fatal(err)
	}
	defer listener.Close()
	fmt.Println("Дух машина внемлит твоим словам!")
	for {
		conn, err := listener.Accept()
		if err != nil {
			log.Fatal(err)
			conn.Close()
			continue
		}
		go handleConnection(conn) // запускаем поток для обработки запроса
	}

}

func handleConnection(conn net.Conn) {
	defer conn.Close()
	for {
		// считываем полученные в запросе данные
		input := make([]byte, (1024 * 64))
		n, err := conn.Read(input)
		if n == 0 || err != nil {
			fmt.Println("Read error:", err)
			break
		}
		source := string(input[0:n])
		// на основании полученных данных получаем из словаря изображение
		target, ok := command[source]
		if !ok { // если данные не найдены в словаре
			target = "imagesom.png"
		}
		// выводим на консоль сервера диагностическую информацию
		fmt.Println(source, "-", target)
		// отправляем данные клиенту
		conn.Write(coder(target))
	}
}

func coder(str string) []byte {
	var img64 []byte
	img64, _ = ioutil.ReadFile("./img/" + str)
	return img64
}
