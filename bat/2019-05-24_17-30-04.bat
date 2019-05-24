for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I
set DATET=%datetime:~0,8%-%datetime:~8,6%

"C:\TEK\Programas\bkSQL\lib\mysqldump.exe" --host="aas" --user="asd" --password="asdasd" asd > "asd"%DATET%.sql
exit