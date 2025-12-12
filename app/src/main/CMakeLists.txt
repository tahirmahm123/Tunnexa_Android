cmake_minimum_required(VERSION 3.4.1)
project("Tunnexa")

# Include OpenVPN CMakeLists
add_subdirectory(openvpn/cpp)

# Include WireGuard CMakeLists  
add_subdirectory(wireguard/tools)

