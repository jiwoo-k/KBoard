$(function(){
   //[추가] 버튼 누르면 첨부파일 추가 기능

    let i = 0;
    $('#btnAdd').click(function(){
        $('#files').append(`
        <div class="input-group mb-2">
            <input class="form-control col-xs-3" type="file" name="upfile${i}"/> <!--file type은 이 태그의 name 값이 unique 해야함-->
            <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
        </div>`);

        i+=1;
    });

    //SummerNote 추가
    $('#content').summernote({
        height: 300,
    });
});