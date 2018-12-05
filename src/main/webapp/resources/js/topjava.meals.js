const ajaxUrl = "ajax/meals/";
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#mealsTable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();
});

function filter() {
    var filter = $("#filter");
    var startdate = filter.find("input#startDate").val();
    var endDate = filter.find("input#endDate").val();
    var startTime = filter.find("input#startTime").val();
    var endTime = filter.find("input#endTime").val();
    var url = ajaxUrl + "filter?";
    url = url + "startDate=" + startdate + "&";
    url = url + "endDate=" + endDate + "&";
    url = url + "startTime=" + startTime + "&";
    url = url + "endTime=" + endTime;
    $.get(url, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });

}

function cleanFilter() {
    $("#filter").find(":input").val("");
}

function updateTable() {
    filter();
}

