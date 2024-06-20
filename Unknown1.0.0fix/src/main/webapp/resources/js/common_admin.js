$(document).ready(function() {
    var newPath = updateActionUrl();
    var actionForm = $("#actionForm");
    var sortColumnValue = $("#sortColumn");
    var groupColumnValue = $("#groupColumn");

    // 페이지네이션 버튼 클릭 이벤트 처리
    $(".paginate_button a").on("click", function(e) {
        e.preventDefault();
        console.log("click");
        var targetPage = $(this).data("page"); // href 대신 data-page 사용
        if (!targetPage) {
            targetPage = $(this).attr("href");
        }
        actionForm.find("input[name='pageNum']").val(targetPage);
        actionForm.attr("action", newPath);
        actionForm.submit();
    });

    // 정렬 버튼 클릭 이벤트 처리
    $(".button-add a").on("click", function(e) {
        e.preventDefault();
        var groupColumn = $(this).data("groupColumn");
        var sortColumn = $(this).data("sortColumn");
        groupColumnValue.val(groupColumn);
        sortColumnValue.val(sortColumn);
        var href = $(this).attr("href");
        actionForm.attr("action", href);
        actionForm.submit();
    });

    // 검색 폼 제출 이벤트 처리
    $("#searchForm button").on("click", function(e) {
        var searchForm = $("#searchForm");
        if (!searchForm.find("select[name='type']").val()) {
            alert("검색종류를 선택하세요");
            return false;
        }
        if (!searchForm.find("input[name='keyword']").val()) {
            alert("키워드를 입력하세요");
            return false;
        }
        searchForm.find("input[name='pageNum']").val("1");
        e.preventDefault();
        searchForm.attr("action", newPath);
        searchForm.submit();
    });
});

function extractPageName(url) {
    var lastSlashIndex = url.lastIndexOf('/');
    var pageName = url.substring(lastSlashIndex + 1);
    return pageName;
}

function removeAction() {
    $("#modifyForm").attr("action", "remove");
    $("#modifyForm").submit();
}

function goToModalForm() {
    clearModalForm();
    $('#formModal').modal('show');
}

function clearModalForm() {
    $('#registerForm')[0].reset();
}

function closeModal(element) {
    $(element).closest('.modal').modal('hide');
}

function validateInput() {
    const input = document.getElementById("itemId");
    const value = input.value;
    const pattern = /^[0-9]+$/;
    if (!pattern.test(value)) {
        alert("숫자만 입력하세요");
        input.focus();
    }
}

function showElement(elementId) {
    var element = document.getElementById(elementId);
    if (element) {
        element.classList.remove("d-none");
        element.classList.add("d-block");
    }
}
