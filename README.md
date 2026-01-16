# Tool Rental System

A tool rental system that lets you rent tools with automatic pricing calculations based on rental duration, tool type, and applicable discounts.

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## How to Build

1. Navigate to the `toolShop` directory:
```bash
cd toolShop
```

2. Clean and build the project using Maven:
```bash
mvn clean install
```

This command will:
- Clean any previous build artifacts
- Compile the source code
- Run all unit tests
- Package the application into a JAR file

## How to Run

1. After building, run the application using Maven:
```bash
mvn spring-boot:run
```

Or, if you prefer to run the built JAR directly:
```bash
java -jar target/toolShop-0.0.1-SNAPSHOT.jar
```

2. The application will start and listen on `http://localhost:8080`

## API Endpoints

### 1. Get All Available Tools

**Request:**
```
GET /api/tools
```

**Response:**
```json
["CHNS", "LADW", "JAKD", "JAKR"]
```

**Description:** Returns a list of all tool codes available for rental.

---

### 2. Rent a Tool

**Request:**
```
POST /api/tools/rental/{toolCode}
Content-Type: application/json

{
  "totalRentalDays": 5,
  "checkoutDate": "01/20/25",
  "discount": 10
}
```

**Path Parameters:**
- `toolCode` (required): The code of the tool to rent (CHNS, LADW, JAKD, or JAKR)

**Request Body Parameters:**
- `totalRentalDays` (required): Number of days to rent (1-100)
- `checkoutDate` (required): Checkout date in MM/dd/yy format
- `discount` (required): Discount percentage (0-100)

**Response (Success - HTTP 201):**
```json
{
  "totalRentalDays": 5,
  "checkoutDate": "01/20/25",
  "dueDate": "01/25/25",
  "chargeDays": 4,
  "discount": 10,
  "preDiscountPrice": 7.96,
  "moneySaved": 0.80,
  "totalPrice": 7.16
}
```

**Response (Error - HTTP 400):**
```json
{
  "error": "The tool code 'INVALID' doesn't exist"
}
```

---

## Example API Calls

### Using cURL

**Get all tools:**
```bash
curl -X GET http://localhost:8080/api/tools
```

**Rent a Chainsaw for 5 days with 10% discount:**
```bash
curl -X POST http://localhost:8080/api/tools/rental/CHNS \
  -H "Content-Type: application/json" \
  -d '{
    "totalRentalDays": 5,
    "checkoutDate": "01/20/25",
    "discount": 10
  }'
```

### Using Postman

1. **Get All Tools**
   - Method: GET
   - URL: `http://localhost:8080/api/tools`

2. **Rent a Tool**
   - Method: POST
   - URL: `http://localhost:8080/api/tools/rental/CHNS`
   - Body (JSON):
     ```json
     {
       "totalRentalDays": 5,
       "checkoutDate": "01/20/25",
       "discount": 10
     }
     ```

## Available Tools

| Code | Tool Type  | Brand  | Daily Charge | Weekday | Weekend | Holiday |
|------|-----------|--------|--------------|---------|---------|---------|
| CHNS | Chainsaw  | Stihl  | $1.99        | Yes     | No      | Yes     |
| LADW | Ladder    | Werner | $1.49        | Yes     | Yes     | No      |
| JAKD | Jackhammer| DeWalt | $2.99        | Yes     | No      | No      |
| JAKR | Jackhammer| Ridgid | $2.99        | Yes     | No      | No      |

## Holiday Observance

The system recognizes the following holidays:
- **Independence Day**: July 4th (or observed date if it falls on a weekend)
- **Labor Day**: First Monday in September

## Validation Rules

- Total rental days must be between 1 and 100
- Discount percentage must be between 0 and 100
- Checkout date must be today or in the future
- Tool code must be valid (CHNS, LADW, JAKD, or JAKR)

## Error Handling

The API will return a 400 Bad Request status with an error message if:
- Invalid tool code is provided
- Rental days are outside the 1-100 range
- Discount is outside the 0-100% range
- Checkout date is in the past
- Required fields are missing

Example error response:
```json
{
  "error": "The rental day must be on or after the current date: 2025-01-20"
}
```