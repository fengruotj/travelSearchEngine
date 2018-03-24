#创建索引
http://ubuntu2:9200/tourindex

#添加索引的结构
http://ubuntu2:9200/tourindex/_mapping/tour/
{
	"properties": {
		"id": {
			"type": "long"
		},
		"title": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"supplier": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"price": {
			"type": "integer"
		},
		"tourLine": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"tourDays": {
			"type": "integer"
		},
		"startCity": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"startMonth": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"describe": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"publishDate": {
			"type": "date"
		}
	}

}
