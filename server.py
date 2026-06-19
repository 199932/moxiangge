#!/usr/bin/env python3
import os
import socket
import sys
from http.server import HTTPServer, SimpleHTTPRequestHandler

PORT = 8000
os.chdir(os.path.dirname(os.path.abspath(__file__)))

def get_lan_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except:
        return "127.0.0.1"

# 动态更新 index.html 中的 IP
def update_html_ip():
    ip = get_lan_ip()
    index_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "index.html")
    try:
        with open(index_path, "r", encoding="utf-8") as f:
            content = f.read()
        # 替换 IP 常量
        import re
        new_content = re.sub(
            r"const IP = '[^']+';",
            f"const IP = '{ip}';",
            content
        )
        if new_content != content:
            with open(index_path, "w", encoding="utf-8") as f:
                f.write(new_content)
            print(f"Updated index.html IP to {ip}")
    except Exception as e:
        print(f"Warning: could not update index.html IP: {e}", file=sys.stderr)
    return ip

class CORSHandler(SimpleHTTPRequestHandler):
    def end_headers(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Cache-Control", "no-cache")
        super().end_headers()

    def log_message(self, format, *args):
        print(f"[{self.log_date_time_string()}] {args[0]}")

if __name__ == "__main__":
    ip = update_html_ip()
    server = HTTPServer(("0.0.0.0", PORT), CORSHandler)
    print(f"\n=== 墨香阁 APK 下载服务已启动 ===")
    print(f"本地访问:  http://127.0.0.1:{PORT}/")
    print(f"手机访问:  http://{ip}:{PORT}/")
    print(f"APK 直链:  http://{ip}:{PORT}/moxiangge.apk")
    print(f"\n手机和电脑请连入同一 WiFi")
    print("按 Ctrl+C 停止服务\n")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        print("\n服务已停止")
        server.server_close()
