#!/usr/bin/python

from bs4 import BeautifulSoup
import os

input_path = "../../data/html_sample"
output_path = "../../data/cleaned"

if not os.path.exists(output_path):
    os.makedirs(output_path)

def is_category_list(tag):
    return tag.name=='ul' and tag.has_attr('id') and tag['id']=='breadcrumb'

def is_category_link(tag):
    return tag.name=='a' and tag.has_attr('title') and tag['title'] != "Main Page" and not tag['title'].startswith("Special") 

def parse_category(tag):
    category_anchors = tag.find_all(is_category_link)
    return map(lambda a: a.get_text() ,category_anchors)

def parse_categories(soup):
    category_listings = soup.find_all(is_category_list)
    all_categories = []
    for category in category_listings:
        all_categories.append(parse_category(category))
    return all_categories

def is_step_listing(tag):
    return tag.name=='ol' and tag.has_attr('class') and tag['class'][0].startswith('steps_list')

def is_step_tag(tag):
    return tag.name=='b' and tag.has_attr('class') and tag['class'][0]=='whb'

def parse_step_listing(tag):
    return map(lambda b: b.get_text(), tag.find_all(is_step_tag))

def parse_step_listings(soup):
    all_step_listings = []
    for step_listing in soup.find_all(is_step_listing):
        all_step_listings.append(parse_step_listing(step_listing))
    return all_step_listings

def formatOutput(out,all_categories,all_step_listings):
    out.write("#Categories\n")

    for cs in all_categories:
        out.write("\t".join(cs)+"\n")

    for sls in all_step_listings:
        out.write("#Steps\n")
        out.write("\n".join(sls)+"\n")

def main():
    for wikihow_page_path in os.listdir(input_path):
        if not wikihow_page_path.endswith(".html"):
            continue
        wikihow_page = open(os.path.join(input_path,wikihow_page_path))
        out = open(os.path.join(output_path,wikihow_page_path+".cld"),'w')
        soup = BeautifulSoup(wikihow_page)
        formatOutput(out,parse_categories(soup),parse_step_listings(soup))
        out.close()

if __name__ == "__main__":
    main()
