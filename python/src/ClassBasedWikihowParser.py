#!/usr/bin/python

from bs4 import BeautifulSoup
from bs4.element import Tag 
from bs4.element import NavigableString
import os
import codecs
import lxml.etree as etree 
import lxml.builder

input_path = "../../data/html_sample"
output_path = "../../data/cleaned"

if not os.path.exists(output_path):
    os.makedirs(output_path)


def is_category_list(tag):
    return tag.name == 'ul' and tag.has_attr('id') and tag['id'] == 'breadcrumb'


def is_category_link(tag):
    return tag.name == 'a' and tag.has_attr('title') and tag['title'] != "Main Page" and not tag['title'].startswith(
        "Special")


def parse_category(tag):
    return map(lambda a: a.get_text(), tag.find_all(is_category_link))


def parse_categories(soup):
    return map(lambda category : parse_category(category), soup.find_all(is_category_list))


def is_step_listing(tag):
    return tag.name == 'ol' and tag.has_attr('class') and tag['class'][0].startswith('steps_list')


def is_step_tag(tag):
    return tag.name == 'b' and tag.has_attr('class') and tag['class'][0] == 'whb'


def is_clear_div(tag):
    return tag.name == 'div' and tag.has_attr('class') and tag['class'][0] == 'clearall'


def parse_ul_text(tag):
    return map(lambda l : unicode(l.get_text()).rstrip(), tag.find_all(lambda t : t.name == 'li'))


def parse_text(tag):
    if type(tag) is NavigableString:
        return unicode(tag.string)
    elif type(tag) is Tag:
        if tag.name == 'ul':
            return parse_ul_text(tag)
        else:
            return tag.get_text()

def parse_step_details(tag):
    s = []
    while not tag is None and not is_clear_div(tag):
        s.append(parse_text(tag))
        tag = tag.next_sibling
    return s


def parse_step_listing(tag):
    return map(lambda b: [b.get_text(),parse_step_details(b.next_sibling)], tag.find_all(is_step_tag))


def parse_step_listings(soup):
    return map(lambda listing: parse_step_listing(listing),  soup.find_all(is_step_listing))


def format_output(out, all_categories, all_step_listings):
    root = etree.Element('root')
   
    doc = etree.SubElement(root,'document')

    categories = etree.SubElement(doc,'categories')
    steps = etree.SubElement(doc,'steps')

    for cs in all_categories:
        category = etree.SubElement(categories,'category')
        for c in cs:
            ontology = etree.SubElement(category,'c')
            ontology.text = c

    for sls in all_step_listings:
        step = etree.SubElement(steps,'step')
        for s in sls:
            em = etree.SubElement(step, 'em')
            em.text = s[0]

            details = etree.SubElement(step,'details')
            for detail_stuff in s[1]:
                detail = etree.SubElement(details,'d')
                if isinstance(detail_stuff,list):
                    for l in detail_stuff:
                        li = etree.SubElement(detail,'l')
                        li.text = l
                else:
                    detail.text = detail_stuff

    et = etree.ElementTree(root)
    et.write(out,xml_declaration=True, encoding='utf-8',pretty_print=True)

def main():
    for wikihow_page_path in os.listdir(input_path):
        if not wikihow_page_path.endswith(".html"):
            continue
        wikihow_page = open(os.path.join(input_path, wikihow_page_path))
        soup = BeautifulSoup(wikihow_page)
        format_output(os.path.join(output_path, wikihow_page_path + ".xml"), parse_categories(soup), parse_step_listings(soup))

if __name__ == "__main__":
    main()
