# LeDuyHungKhanh-E16CN-B16DCVT172
Bài thi cuối kỳ môn Phát triển ứng dụng di động - GV: Trịnh Thị Vân Anh,
Sinh viên thực hiện: Lê Duy Hưng Khánh,
MSV: B16DCVT172 - lớp E16CN,

************************************************************************
ỨNG DỤNG: APP NGHE NHẠC TRÊN THIẾT BỊ DI ĐỘNG ANDROID
Tính năng app:
* Fragment:
dùng view pager + fragmentpageradapter,
list tất cả bài hát trong bộ nhớ,
list tất cả nhạc sỹ (ấn vào sẽ hiện các bài hát của nhạc sỹ đó),
playlist đã tạo,

* Activity MainPlayer điều khiển nhạc:
Seekbar dùng để tua nhạc,
play/pause, next previous bài nhạc,
chạy thread với bài có tên dài,

* Search bài hát trong list nhạc:
gõ để tìm kiếm

* SQLite để tạo, thêm bài hát vào playlist:
ở Fragment All song, nhấn giữ 1 bài nhạc,
chọn add to playlist,
click create new playlist để tạo playlist mới -> nhập tên playlist mới và thêm bài hát vừa chọn vào,
hoặc chọn một playlist có sẵn ở dưới để thêm bài nhạc vào playlist đó

* Webview search video (yêu cầu internet):
ở fragment All songs, ấn giữ 1 bài nhạc, chọn search for video,
chuyển đến activity webview hiện thị kết quả tìm kiếm bài hát đó trên google

* Chơi nhạc:
service chạy ngầm,
notification(hiện thông tin bài hát đang phát),
chọn một bài hát bất kỳ để chơi ở tất cả activity hay fragment,
phát trộn bài nhạc(Shuffle)
