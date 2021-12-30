#include <assert.h>
#include <ctype.h>
#include <limits.h>
#include <stdarg.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/errno.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <unistd.h>
#include <fcntl.h>

// stub, for tests
struct bytes_array {
    char *data;
    size_t size;
};

int hb_read_byte(int fd);
int hb_open(char *filename);
int hb_check_is_file(int fd);
int hb_read_bytes(int fd, struct bytes_array *buffer, size_t count);

/// size_t read(int fd, void *buffer, size_t size);
///
/// A value of zero indicates end-of-file (except if the value of the size argument is also zero).
/// This is not considered an error.
/// If you keep calling read while at end-of-file, it will keep returning zero and doing nothing else.
///
/// If read returns at least one character, there is no way you can tell whether end-of-file was reached.
/// But if you did reach the end, the next read will return zero.
///
/// In case of an error, read returns -1.

int hb_read_byte(int fd)
{
    assert(fd > 0);

    char ret = '\0';
    ptrdiff_t nread = read(fd, &ret, 1u);

    // EOF
    if (nread == 0) {
        return -1;
    }

    // an error was occured
    if (nread == -1) { /* error */
        assert(0 && "read error");
    }

    return ret & 0xFF;
}

/// int open (const char *filename, int flags[, mode_t mode])
///
/// The open function creates and returns a new file descriptor for the file named by filename.
/// Initially, the file position indicator for the file is at the beginning of the file.
/// The argument mode is used only when a file is created, but it doesn't hurt to supply the argument in any case.
///
/// The flags argument controls how the file is to be opened.
/// This is a bit mask; you create the value by the bitwise OR of the appropriate parameters (using the | operator in C). , for the parameters available.
///
/// The normal return value from open is a non-negative integer file descriptor.
/// In the case of an error, a value of -1 is returned instead.

int hb_open(char *filename)
{
    assert(filename);

    int fd = open(filename, O_RDONLY);
    assert(fd > 0);
    assert(hb_check_is_file(fd));

    return fd;
}

/// int stat (const char *filename, struct stat *buf)
///
/// The stat function returns information about the attributes of the file named by filename in the structure pointed to by buf.
/// If filename is the name of a symbolic link, the attributes you get describe the file that the link points to.
/// If the link points to a nonexistent file name, then stat fails reporting a nonexistent file.
/// The return value is 0 if the operation is successful, or -1 on failure.
///
/// int fstat (int filedes, struct stat *buf)
///
/// The fstat function is like stat, except that it takes an open file descriptor as an argument instead of a file name.
/// Like stat, fstat returns 0 on success and -1 on failure.

int hb_check_is_file(int fd)
{
    assert(fd > 0);

    struct stat statbuf;
    int ret = fstat(fd, &statbuf);
    if (ret < 0) {
        return 0;
    }

    mode_t mode = statbuf.st_mode;
    if (!S_ISREG(mode)) {
        return 0;
    }

    return 1;
}

int hb_get_file_size(int fd, size_t *result)
{
    assert(fd > 0);
    assert(result);

    struct stat statbuf;
    int ret = fstat(fd, &statbuf);
    if (ret < 0) {
        return 0;
    }

    *result = (size_t) statbuf.st_size;
    return 1;
}

int hb_read_bytes(int fd, struct bytes_array *buffer, size_t count)
{
    assert(fd > 0);
    assert(buffer);
    assert(buffer->data);
    assert(count);

    // 1) the count > allocated
    if ((count + 1) >= buffer->size) {
        assert(0 && "the buffer is not big enough.");
    }

    ptrdiff_t nread = read(fd, buffer->data, count);
    if (nread == -1) {
        assert(0 && "cannot read from the file.");
    }

    buffer->data[buffer->size] = '\0';
    return 1;
}

/// int close (int filedes);
///
/// The normal return value from close is 0.
/// A value of -1 is returned in case of failure.

void hb_close(int fd)
{
    assert(fd > 0);
    int result = close(fd);
    assert(result == 0);
}

int main(int argc, char **argv)
{
    char *filename = "data.txt";
    int fd = hb_open(filename);

    struct bytes_array *bytes = calloc(1, sizeof(struct bytes_array));
    bytes->size = 12;
    bytes->data = (char*) calloc(1, (sizeof(char) * bytes->size) + 1);

    hb_read_bytes(fd, bytes, 5);
    printf("%s\n", bytes->data);

    hb_close(fd);

    printf("\n:ok:\n");
    return 0;
}
