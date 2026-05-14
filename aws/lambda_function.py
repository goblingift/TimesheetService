import json
from pathlib import Path

BASE_DIR = Path(__file__).parent


def lambda_handler(event, context):
    params = event.get("queryStringParameters") or {}
    month = params.get("month")
    year = params.get("year")

    if not month or not year:
        return {
            "statusCode": 400,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": "Please provide query parameters month and year."
            })
        }

    try:
        month_int = int(month)
        year_int = int(year)
    except ValueError:
        return {
            "statusCode": 400,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": "month and year must be integers."
            })
        }

    file_name = f"fake-timesheets-{year_int}-{month_int:02d}.json"
    data_file = BASE_DIR / file_name

    if not data_file.exists():
        return {
            "statusCode": 404,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": f"No demo timesheet data found for {year_int}-{month_int:02d}."
            })
        }

    with open(data_file, "r", encoding="utf-8") as file:
        timesheet_entries = json.load(file)

    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "body": json.dumps(timesheet_entries)
    }