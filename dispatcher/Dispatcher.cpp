#include <iostream>
#include <unordered_map>
#include <string>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <vector>
#include <algorithm> 

#pragma comment(lib, "ws2_32.lib")

const int PORT = 5000;
const int BUFFER_SIZE = 1024;

int main() {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        std::cerr << "Winsock initialization error\n";
        return 1;
    }

    std::unordered_map<std::string, std::vector<std::string>> server_table;  // Allow multiple servers per type

    SOCKET server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd == INVALID_SOCKET) {
        std::cerr << "Error creating socket: " << WSAGetLastError() << "\n";
        WSACleanup();
        return 1;
    }

    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, (char*)&opt, sizeof(opt));

    sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    if (bind(server_fd, (sockaddr*)&address, sizeof(address)) == SOCKET_ERROR) {
        std::cerr << "Error at bind: " << WSAGetLastError() << "\n";
        closesocket(server_fd);
        WSACleanup();
        return 1;
    }

    if (listen(server_fd, 5) == SOCKET_ERROR) {
        std::cerr << "Error on listen: " << WSAGetLastError() << "\n";
        closesocket(server_fd);
        WSACleanup();
        return 1;
    }

    std::cout << "Dispatcher listen on port " << PORT << "...\n";

    while (true) {
        sockaddr_in client_addr;
        int addr_len = sizeof(client_addr);

        SOCKET client_socket = accept(server_fd, (sockaddr*)&client_addr, &addr_len);
        if (client_socket == INVALID_SOCKET) {
            std::cerr << "Error accepting: " << WSAGetLastError() << "\n";
            continue;
        }

        char buffer[BUFFER_SIZE] = { 0 };
        int bytes_read = recv(client_socket, buffer, BUFFER_SIZE, 0);
        if (bytes_read <= 0) {
            closesocket(client_socket);
            continue;
        }

        std::string request(buffer);
        request.erase(std::remove(request.begin(), request.end(), '\n'), request.end());
        request.erase(std::remove(request.begin(), request.end(), '\r'), request.end());

        std::cout << "Received: " << request << "\n";

        std::string response = "NOT_FOUND";

        if (request.rfind("Register:", 0) == 0) {
            // Format: "Register: ServerType Port"
            size_t first_space = request.find(' ');
            size_t second_space = request.find(' ', first_space + 1);

            if (first_space != std::string::npos && second_space != std::string::npos) {
                std::string server_type = request.substr(first_space + 1, second_space - first_space - 1);
                std::string port = request.substr(second_space + 1);

                char client_ip[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &client_addr.sin_addr, client_ip, INET_ADDRSTRLEN);

                std::string address_str = std::string(client_ip) + ":" + port;

                server_table[server_type].push_back(address_str);

                response = "REGISTERED " + server_type;
                std::cout << "Registered server: " << server_type << " -> " << address_str << "\n";
            }
            else {
                response = "INVALID_REGISTER_FORMAT";
            }
        }
        else {
            std::string server_type = request;
            server_type.erase(std::remove(server_type.begin(), server_type.end(), ' '), server_type.end());
            if (server_table.find(server_type) != server_table.end() && !server_table[server_type].empty()) {
                response = server_table[server_type].front();  
            }
        }

        send(client_socket, response.c_str(), response.size(), 0);
        closesocket(client_socket);
    }

    closesocket(server_fd);
    WSACleanup();
    return 0;
}
