window.onload = function flowerLike() {
    $("#request").ready(function () {
        $('.btn-like').click(function (e) {
            e.preventDefault();
            let post_id = $(this).data('id');
            let url = $(this).data('/flower/like');
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            var name = $("#userName").val();

            $.ajax({
                type: 'POST',
                url: url,
                data: {
                    post_id: $("#post_id"),
                    like: $("#like"),
                    csrfmiddlewaretoken: $('input[name=csrfmiddlewaretoken]').val()
                },
                success: function (response) {
                    if (response['status'] == 'liked') {
                        $('.btn-like[data-id=' + post_id + ']').addClass('btn-liked');
                    } else {
                        $('.btn-like[data-id=' + post_id + ']').removeClass('btn-liked');
                    }
                    $('.like-count[data-id=' + post_id + ']').text(response['like_count']);
                },
                error: function (response) {
                    console.log('error', response);
                }
            });
        });
    });
}
